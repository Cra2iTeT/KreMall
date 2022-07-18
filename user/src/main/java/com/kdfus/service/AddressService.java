package com.kdfus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kdfus.domain.dto.user.AddressDTO;
import com.kdfus.domain.entity.user.Address;
import com.kdfus.domain.vo.user.AddressVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Cra2iTeT
 * @date 2022/7/18 15:53
 */
public interface AddressService extends IService<Address> {
    String add(AddressDTO addressDTO);

    String update(AddressDTO addressDTO);

    AddressVO getDefault(HttpServletRequest request);
}
