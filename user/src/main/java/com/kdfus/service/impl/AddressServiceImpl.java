package com.kdfus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdfus.domain.dto.user.AddressDTO;
import com.kdfus.domain.entity.user.Address;
import com.kdfus.domain.vo.user.AddressVO;
import com.kdfus.domain.vo.user.UserVO;
import com.kdfus.mapper.AddressMapper;
import com.kdfus.service.AddressService;
import com.kdfus.system.ServiceResultEnum;
import com.kdfus.util.NumberUtils;
import com.kdfus.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.concurrent.TimeUnit;

import static com.kdfus.system.RedisConstants.*;

/**
 * @author Cra2iTeT
 * @date 2022/7/18 15:53
 */
@Service("Address")
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String save(String token, AddressDTO addressDTO) {
        Address address = BeanUtil.copyProperties(addressDTO, Address.class);
        address.setId(NumberUtils.genId());
        UserVO userVO = TokenUtils.verify(token, stringRedisTemplate);
        // 添加默认地址
        if (address.getIsDefault().intValue() == 1) {
            // 删除原默认地址
            LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(Address::getIsDefault, (byte) 0).eq(Address::getIsDefault, (byte) 1).last("for update");
            if (!update(wrapper)) {
                return ServiceResultEnum.UPDATE_ERROR.getResult();
            }
            stringRedisTemplate.delete(USER_ADDRESS_DEFAULT_KEY + userVO.getAccountId());
        }
        if (save(address)) {
            return null;
        }
        return ServiceResultEnum.OPERATE_ERROR.getResult();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String update(String token, Long id) {
        UserVO userVO = TokenUtils.verify(token, stringRedisTemplate);
        // 修改原默认地址
        LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Address::getIsDefault, (byte) 0).eq(Address::getIsDefault, (byte) 1).last("for update");
        if (!update(wrapper)) {
            return ServiceResultEnum.UPDATE_ERROR.getResult();
        }
        stringRedisTemplate.delete(USER_ADDRESS_DEFAULT_KEY + userVO.getAccountId());
        wrapper.clear();
        wrapper.set(Address::getIsDefault, (byte) 1).eq(Address::getId, id);
        if (!update(wrapper)) {
            return ServiceResultEnum.UPDATE_ERROR.getResult();
        }
        return null;
    }

    @Override
    public String update(String token, AddressDTO addressDTO) {
        UserVO userVO = new UserVO();
        LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
        Address address = BeanUtil.copyProperties(addressDTO, Address.class);
        wrapper.set(address.getName() != null, Address::getName, addressDTO.getName())
                .set(address.getPhone() != null, Address::getPhone, address.getPhone())
                .set(address.getProvince() != null, Address::getProvince, address.getProvince())
                .set(address.getCity() != null, Address::getCity, address.getCity())
                .set(address.getRegion() != null, Address::getRegion, address.getRegion())
                .set(address.getDetail() != null, Address::getDetail, address.getDetail())
                .eq(Address::getId, address.getId());
        if (update(wrapper)) {
            stringRedisTemplate.delete(USER_ADDRESS_USUAL_KEY + userVO.getAccountId() + ":" + address.getId());
            return null;
        }
        return ServiceResultEnum.OPERATE_ERROR.getResult();
    }

    @Override
    public AddressVO getDefault(String token) {
        UserVO userVO = TokenUtils.verify(token, stringRedisTemplate);
        String addressVOJson = stringRedisTemplate.opsForValue().get(USER_ADDRESS_DEFAULT_KEY + userVO.getAccountId());
        if (addressVOJson != null) {
            AddressVO addressVO = JSON.parseObject(addressVOJson, AddressVO.class);
            return addressVO;
        }
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getIsDefault, (byte) 0);
        Address address = getOne(wrapper);
        if (address != null) {
            AddressVO addressVO = BeanUtil.copyProperties(address, AddressVO.class);
            stringRedisTemplate.opsForValue().set(USER_ADDRESS_DEFAULT_KEY + userVO.getAccountId(),
                    JSON.toJSONString(userVO), USER_ADDRESS_DEFAULT_TTL, TimeUnit.MINUTES);
            return BeanUtil.copyProperties(address, AddressVO.class);
        }
        return null;
    }
}
