package com.kdfus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdfus.system.ServiceResultEnum;
import com.kdfus.domain.dto.LoginDTO;
import com.kdfus.domain.dto.RegistryDTO;
import com.kdfus.domain.entity.user.User;
import com.kdfus.domain.vo.UserVO;
import com.kdfus.mapper.UserMapper;
import com.kdfus.service.UserService;
import com.kdfus.util.MD5Util;
import com.kdfus.util.NumberUtil;
import com.kdfus.util.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.kdfus.system.Constants.FILE_USER_UPLOAD_DIC;
import static com.kdfus.system.Constants.USER_NICK_NAME_PREFIX;
import static com.kdfus.system.RedisConstants.*;

/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/6/25 19:14
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String login(LoginDTO loginDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        String accountId = loginDTO.getAccountId();
        String passwordMd5 = loginDTO.getPasswordMd5();

        if (passwordMd5 == null) {
            return ServiceResultEnum.LOGIN_NULL.getResult();
        }

        wrapper.eq(User::getAccountId, accountId)
                .eq(User::getPasswordMd5, MD5Util.MD5Encode(passwordMd5, "UTF-8"));
        List<User> userList = list(wrapper);

        if (userList != null && userList.size() == 1) {
            User user = userList.get(0);
            String token = NumberUtil.genToken(user.getId());
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            // 映射多端登录信息
            // 先查询当前有几台设备登录
            Set<String> keys = stringRedisTemplate.keys(LOGIN_USER_INFO_KEY + userVO.getAccountId() + "*");
            if (keys != null && keys.size() == 3) {
                Object[] array = keys.toArray();
                Long[] expire = new Long[3];
                for (int i = 0; i < 3; i++) {
                    // 避免ttl恰好过期，expire获取null情况
                    Long tempExpire = stringRedisTemplate.opsForValue().getOperations().getExpire(array[i].toString());
                    expire[i] = tempExpire == null ? -1 : tempExpire;
                }

                int tempIndex = expire[0] < expire[1] ? 0 : 1;
                Long minExpire = expire[tempIndex];

                tempIndex = minExpire < expire[2] ? tempIndex : 2;
                // 删除最不常用的设备登录信息
                stringRedisTemplate.delete(LOGIN_USER_INFO_KEY + userVO.getAccountId() + ":" + token);
                stringRedisTemplate.delete(LOGIN_USER_TOKEN_KEY + array[tempIndex].toString());
            }
            // 云端存储token
            stringRedisTemplate.opsForValue()
                    .set(LOGIN_USER_TOKEN_KEY + token, JSON.toJSONString(userVO), LOGIN_USER_TOKEN_TTL, TimeUnit.MINUTES);
            stringRedisTemplate.opsForValue()
                    // 映射token与登录设备关系
                    .set(LOGIN_USER_INFO_KEY + userVO.getAccountId() + ":" + token, "", LOGIN_USER_INFO_TTL, TimeUnit.MINUTES);
            return token;
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public Boolean logout(String token) {
        return Boolean.TRUE.equals(stringRedisTemplate.delete(LOGIN_USER_TOKEN_KEY))
                && Boolean.TRUE.equals(stringRedisTemplate.delete(LOGIN_USER_INFO_KEY));
    }

    @Override
    @Transactional
    public String registry(RegistryDTO registryDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        String accountId = registryDTO.getAccountId();
        String passwordMd5 = registryDTO.getPasswordMd5();
        String confirmPasswordMd5 = registryDTO.getConfirmPasswordMd5();

        if (passwordMd5 == null || !passwordMd5.equals(confirmPasswordMd5)) {
            return ServiceResultEnum.PASSWORD_DIFFERENT.getResult();
        }

        // 先从映射关系查询是否已经存在账号
        String userInfo = stringRedisTemplate.opsForValue().get(LOGIN_USER_INFO_KEY + accountId);
        if (userInfo != null) {
            return ServiceResultEnum.EXISTED.getResult();
        }

        wrapper.eq(User::getAccountId, accountId)
                .eq(User::getPasswordMd5, MD5Util.MD5Encode(passwordMd5, "UTF-8"));
        List<User> userList = list(wrapper);
        if (userList.size() == 1) {
            return ServiceResultEnum.EXISTED.getResult();
        }
        User user = new User();
        Long id = Long.valueOf(NumberUtil.genNo());
        String token = NumberUtil.genToken(id);
        user.setId(id);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        user.setAccountId(registryDTO.getAccountId());
        passwordMd5 = MD5Util.MD5Encode(passwordMd5, "UTF-8");
        user.setPasswordMd5(passwordMd5);

        if (save(user)) {
            return null;
        }
        return "账号注册失败！";
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String update(String token, String nickName) {
        String key = LOGIN_USER_TOKEN_KEY + token;
        String userVOJSON = stringRedisTemplate.opsForValue().get(key);
        // 避免token恰好过期
        if (userVOJSON == null) {
            return ServiceResultEnum.UPDATE_ERROR.getResult();
        }
        UserVO userVO = JSON.parseObject(userVOJSON, UserVO.class);
        userVO.setNickName(nickName);

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userVO.getId())
                .set(User::getNickName, userVO.getNickName())
                .last("for update");
        // 添加行锁，避免多线程修改问题
        if (update(wrapper)) {
            // 连同映射关系的ttl一同修改，避免映射过期token未过期导致token信息积累
            Long expire = stringRedisTemplate.opsForValue().getOperations().getExpire(key);
            stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(userVO), expire, TimeUnit.MINUTES);
            stringRedisTemplate.opsForValue().set(LOGIN_USER_INFO_KEY + userVO.getAccountId(), "", expire, TimeUnit.MINUTES);
            return null;
        }
        return ServiceResultEnum.UPDATE_ERROR.getResult();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String update(String token, String oldPasswordMd5, String newPasswordMd5, String confirmPasswordMd5) {
        if (oldPasswordMd5 == null || newPasswordMd5 == null || confirmPasswordMd5 == null) {
            return ServiceResultEnum.PASSWORD_NULL.getResult();
        } else if (oldPasswordMd5.equals(newPasswordMd5)) {
            return ServiceResultEnum.PASSWORD_SAME.getResult();
        } else if (newPasswordMd5.equals(confirmPasswordMd5)) {
            return ServiceResultEnum.PASSWORD_DIFFERENT.getResult();
        }

        String key = LOGIN_USER_TOKEN_KEY + token;
        String userVOJSON = stringRedisTemplate.opsForValue().get(key);
        // 避免token恰好过期
        if (userVOJSON == null) {
            return ServiceResultEnum.UPDATE_ERROR.getResult();
        }
        UserVO userVO = JSON.parseObject(userVOJSON, UserVO.class);

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userVO.getId())
                .set(User::getPasswordMd5, newPasswordMd5)
                .last("for update");

        if (update(wrapper)) {
            // 踢所有用户下线
            Set<String> keys = stringRedisTemplate.keys(LOGIN_USER_INFO_KEY + userVO.getAccountId() + "*");
            Object[] array = keys.toArray();
            // 先删除token
            for (Object o : array) {
                stringRedisTemplate.delete(LOGIN_USER_TOKEN_KEY + o);
            }
            stringRedisTemplate.delete(LOGIN_USER_INFO_KEY + userVO.getAccountId());
            return null;
        }
        return ServiceResultEnum.UPDATE_ERROR.getResult();
    }

    @Override
    public String update(String token, MultipartFile file) {
        String userVOJSON = stringRedisTemplate.opsForValue().get(LOGIN_USER_TOKEN_KEY + token);
        // 避免token恰好过期
        if (userVOJSON == null) {
            return ServiceResultEnum.UPDATE_ERROR.getResult();
        }
        UserVO userVO = JSON.parseObject(userVOJSON, UserVO.class);

        String uploadResult = UploadUtil
                .uploadFile(file, FILE_USER_UPLOAD_DIC + userVO.getAccountId() + "\\");
        if (uploadResult != null) {
            userVO.setUserImg(uploadResult);
            LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(User::getId, userVO.getId())
                    .set(User::getUserImg, userVO.getUserImg());
            if (update(wrapper)) {
                return null;
            }
            return ServiceResultEnum.UPDATE_ERROR.getResult();
        }
        return ServiceResultEnum.UPDATE_ERROR.getResult();
    }

    @Override
    public String getInfo(String token) {
        String userVOJSON = stringRedisTemplate.opsForValue().get(LOGIN_USER_TOKEN_KEY + token);
        // 避免token恰好过期
        if (userVOJSON == null) {
            return null;
        }
        return userVOJSON;
    }

}
