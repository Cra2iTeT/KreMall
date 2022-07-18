package com.kdfus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdfus.domain.dto.user.AddressDTO;
import com.kdfus.domain.entity.user.Address;
import com.kdfus.domain.vo.user.AddressVO;
import com.kdfus.mapper.AddressMapper;
import com.kdfus.service.AddressService;
import com.kdfus.util.NumberUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Cra2iTeT
 * @date 2022/7/18 15:53
 */
@Service("Address")
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {
    @Override
    public String add(AddressDTO addressDTO) {
        Address address = BeanUtil.copyProperties(addressDTO, Address.class);
        address.setId(NumberUtils.genId());
        if (save(address)) {
            return null;
        }
        return "地址保存失败";
    }

    @Override
    public String update(AddressDTO addressDTO) {
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
        return "收货地址操作失败";
    }

    @Override
    public AddressVO getDefault(HttpServletRequest request) {

        return null;
    }
}
