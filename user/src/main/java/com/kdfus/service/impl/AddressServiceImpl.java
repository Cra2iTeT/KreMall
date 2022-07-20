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
import com.kdfus.service.*;
import com.kdfus.system.ServiceResultEnum;
import com.kdfus.util.NumberUtils;
import com.kdfus.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Cra2iTeT
 * @date 2022/7/18 15:53
 */
@Service("Address")
@Slf4j
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CityService cityService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private StreetService streetService;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String save(String token, AddressDTO addressDTO) {
        UserVO userVO = tokenUtils.verifyUser(token);

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
        UserVO userVO = tokenUtils.verifyUser(token);
        LambdaUpdateWrapper<Address> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Address::getIsDefault, (byte) 0).eq(Address::getIsDefault, (byte) 1)
                .eq(Address::getUserId, userVO.getId()).last("for update");
        if (!update(wrapper)) {
            return ServiceResultEnum.UPDATE_ERROR.getResult();
        }
        wrapper.clear();
        wrapper.set(Address::getIsDefault, (byte) 1).eq(Address::getId, id);
        if (!update(wrapper)) {
            return ServiceResultEnum.UPDATE_ERROR.getResult();
        }
        return null;
    }

    @Override
    public List<AddressVO> getList(String token) {
        UserVO userVO = tokenUtils.verifyUser(token);
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getIsDefault, (byte) 0).eq(Address::getIsDeleted, (byte) 0)
                .eq(Address::getUserId, userVO.getId());
        List<Address> addressList = list(wrapper);
        if (addressList.size() != 0) {
            return addressList.stream()
                    .map(item -> {
                        AddressVO addressVO = BeanUtil.copyProperties(item, AddressVO.class);
                        addressVO.setProvince(provinceService.getProvince(item.getProvinceId()).getName());
                        addressVO.setCity(cityService.getCity(item.getCityId()).getName());
                        addressVO.setRegion(regionService.getRegion(item.getRegionId()).getName());
                        addressVO.setStreet(streetService.getStreet(item.getStreetId()).getName());
                        return addressVO;
                    }).toList();
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
                .set(address.getProvinceId() != null, Address::getProvinceId, address.getProvinceId())
                .set(address.getCityId() != null, Address::getCityId, address.getCityId())
                .set(address.getRegionId() != null, Address::getRegionId, address.getRegionId())
                .set(address.getStreetId() != null, Address::getStreetId, address.getStreetId())
                .set(address.getDetail() != null, Address::getDetail, address.getDetail())
                .eq(Address::getId, address.getId());
        if (update(wrapper)) {
            return null;
        }
        return ServiceResultEnum.OPERATE_ERROR.getResult();
    }

    @Override
    public AddressVO getDefault(String token) {
        UserVO userVO = tokenUtils.verifyUser(token);
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
