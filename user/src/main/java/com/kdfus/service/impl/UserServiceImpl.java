package com.kdfus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdfus.domain.dto.LoginDTO;
import com.kdfus.domain.dto.RegistryDTO;
import com.kdfus.domain.entity.user.User;
import com.kdfus.domain.vo.user.UserVO;
import com.kdfus.mapper.UserMapper;
import com.kdfus.service.UserService;
import com.kdfus.system.ServiceResultEnum;
import com.kdfus.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String login(LoginDTO loginDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        String accountId = loginDTO.getAccountId();
        String passwordMd5 = loginDTO.getPasswordMd5();

        if (passwordMd5 == null) {
            return ServiceResultEnum.LOGIN_FORM_NULL.getResult();
        }

        wrapper.eq(User::getAccountId, accountId).eq(User::getIsDeleted, (byte) 0)
                .eq(User::getPasswordMd5, MD5Utils.MD5Encode(passwordMd5, "UTF-8"));
        User user = getOne(wrapper);

        if (user != null) {
            String token = NumberUtils.genToken(user.getId());
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            // ????????????????????????
            // ????????????????????????????????????
            Set<String> keys = redisUtils.fuzzyMatch(LOGIN_USER_MAPPING_KEY + userVO.getAccountId() + "*");
            if (keys != null && keys.size() == 3) {
                Object[] array = keys.toArray();
                Long[] expire = new Long[3];
                for (int i = 0; i < 3; i++) {
                    // ??????ttl???????????????expire??????null??????
                    Long tempExpire = stringRedisTemplate.opsForValue().getOperations().getExpire(array[i].toString());
                    expire[i] = tempExpire == null ? -1 : tempExpire;
                }

                int tempIndex = expire[0] < expire[1] ? 0 : 1;
                Long minExpire = expire[tempIndex];

                tempIndex = minExpire < expire[2] ? tempIndex : 2;
                // ???????????????????????????????????????
                stringRedisTemplate.delete(LOGIN_USER_MAPPING_KEY + userVO.getAccountId() + ":" + token);
                stringRedisTemplate.delete(LOGIN_USER_TOKEN_KEY + array[tempIndex].toString());
            }
            // ????????????token
            stringRedisTemplate.opsForValue().set(LOGIN_USER_TOKEN_KEY + token, JSON.toJSONString(userVO), LOGIN_USER_TOKEN_TTL, TimeUnit.MINUTES);
            stringRedisTemplate.opsForValue()
                    // ??????token?????????????????????
                    .set(LOGIN_USER_MAPPING_KEY + userVO.getAccountId() + ":" + token, "", USER_TTL, TimeUnit.MINUTES);
            return token;
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public Boolean logout(String token) {
        UserVO userVO = tokenUtils.verifyUser(token);
        return Boolean.TRUE.equals(stringRedisTemplate.delete(LOGIN_USER_TOKEN_KEY + token))
                && Boolean.TRUE.equals(stringRedisTemplate.delete(LOGIN_USER_MAPPING_KEY
                + userVO.getAccountId() + ":" + token));
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

        // ????????????????????????????????????????????????
        String userInfo = stringRedisTemplate.opsForValue().get(LOGIN_USER_MAPPING_KEY + accountId);
        if (userInfo != null) {
            return ServiceResultEnum.EXISTED.getResult();
        }

        wrapper.eq(User::getAccountId, accountId).eq(User::getIsDeleted, (byte) 0)
                .eq(User::getPasswordMd5, MD5Utils.MD5Encode(passwordMd5, "UTF-8"));
        User user = new User();
        user = getOne(wrapper);
        if (user != null) {
            return ServiceResultEnum.EXISTED.getResult();
        }
        user.setId(NumberUtils.genId());
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        user.setAccountId(registryDTO.getAccountId());
        passwordMd5 = MD5Utils.MD5Encode(passwordMd5, "UTF-8");
        user.setPasswordMd5(passwordMd5);

        if (save(user)) {
            return null;
        }
        return ServiceResultEnum.OPERATE_ERROR.getResult();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String update(String token, String nickName) {
        UserVO userVO = tokenUtils.verifyUser(token);
        String key = LOGIN_USER_TOKEN_KEY + token;
        userVO.setNickName(nickName);

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userVO.getId()).set(User::getNickName, userVO.getNickName())
                .last("for update");
        // ??????????????????????????????????????????
        if (update(wrapper)) {
            // ?????????????????????ttl?????????????????????????????????token???????????????token????????????
            Long expire = stringRedisTemplate.opsForValue().getOperations().getExpire(key);
            stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(userVO), expire, TimeUnit.MINUTES);
            stringRedisTemplate.opsForValue().set(LOGIN_USER_MAPPING_KEY + userVO.getAccountId(),
                    "", expire, TimeUnit.MINUTES);
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

        UserVO userVO = tokenUtils.verifyUser(token);

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userVO.getId()).eq(User::getIsDeleted, (byte) 0)
                .set(User::getPasswordMd5, newPasswordMd5).last("for update");

        if (update(wrapper)) {
            // ?????????????????????
            Set<String> keys = redisUtils.fuzzyMatch(LOGIN_USER_MAPPING_KEY + userVO.getAccountId() + "*");
            Object[] array = keys.toArray();
            // ?????????token
            for (Object o : array) {
                stringRedisTemplate.delete(LOGIN_USER_TOKEN_KEY + o);
            }
            stringRedisTemplate.delete(LOGIN_USER_MAPPING_KEY + userVO.getAccountId());
            return null;
        }
        return ServiceResultEnum.UPDATE_ERROR.getResult();
    }

    @Override
    public String update(String token, MultipartFile file) {
        UserVO userVO = tokenUtils.verifyUser(token);

        String uploadResult = UploadUtils.uploadFile(file, FILE_USER_UPLOAD_DIC + userVO.getAccountId() + "\\");
        if (uploadResult != null) {
            userVO.setUserImg(uploadResult);
            LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(User::getId, userVO.getId()).set(User::getUserImg, userVO.getUserImg());
            if (update(wrapper)) {
                return null;
            }
            return ServiceResultEnum.UPDATE_ERROR.getResult();
        }
        return ServiceResultEnum.UPDATE_ERROR.getResult();
    }

    @Override
    public String getInfo(String token) {
        return JSON.toJSONString(tokenUtils.verifyUser(token));
    }

}
