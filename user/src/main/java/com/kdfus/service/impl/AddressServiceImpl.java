package com.kdfus.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cra2iTeT
 * @date 2022/7/18 15:53
 */
@Service("Address")
@Slf4j
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String save(String token, AddressDTO addressDTO) {
        UserVO userVO = tokenUtils.verify(token);

        Address address = BeanUtil.copyProperties(addressDTO, Address.class);
        address.setId(NumberUtils.genId());
        address.setUserId(userVO.getId());

        if (address.getIsDefault().intValue() == 1) {
            LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(Address::getIsDefault, (byte) 0).eq(Address::getIsDefault, (byte) 1)
                    .eq(Address::getUserId, userVO.getId()).last("for update");
            if (!update(wrapper)) {
                return ServiceResultEnum.UPDATE_ERROR.getResult();
            }
        }
        if (save(address)) {
            return null;
        }
        return ServiceResultEnum.OPERATE_ERROR.getResult();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String update(String token, Long id) {
        UserVO userVO = tokenUtils.verify(token);
        LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Address::getIsDefault, (byte) 0).eq(Address::getIsDefault, (byte) 1)
                .eq(Address::getUserId, userVO.getId()).last("for update");
        if (!update(wrapper)) {
            return ServiceResultEnum.UPDATE_ERROR.getResult();
        }
        wrapper.clear();
        wrapper.set(Address::getIsDefault, (byte) 1).eq(Address::getId, id).eq(Address::getId, id);
        if (!update(wrapper)) {
            return ServiceResultEnum.UPDATE_ERROR.getResult();
        }
        return null;
    }

    @Override
    public List<AddressVO> getList(String token) {
        UserVO userVO = tokenUtils.verify(token);
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getIsDefault, (byte) 0).eq(Address::getIsDeleted, (byte) 0)
                .eq(Address::getUserId, userVO.getId());
        List<Address> addressList = list(wrapper);
        if (addressList.size() != 0) {
            return addressList.stream().
                    map(item -> BeanUtil.copyProperties(item, AddressVO.class)).toList();
        }
        return null;
    }

    @Override
    public String delete(Long id) {
        LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Address::getIsDeleted, (byte) 1).eq(Address::getId, id);
        if (update(wrapper)) {
            return null;
        }
        return ServiceResultEnum.OPERATE_ERROR.getResult();
    }

    @Override
    public String update(String token, AddressDTO addressDTO) {
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
            return null;
        }
        return ServiceResultEnum.OPERATE_ERROR.getResult();
    }

    @Override
    public AddressVO getDefault(String token) {
        UserVO userVO = tokenUtils.verify(token);
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getIsDefault, (byte) 1).eq(Address::getIsDeleted, (byte) 0)
                .eq(Address::getUserId, userVO.getId());
        Address address = getOne(wrapper);
        if (address != null) {
            return BeanUtil.copyProperties(address, AddressVO.class);
        }
        return null;
    }
}
