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

    AddressVO getDefault(String token);

    String update(String token, AddressDTO addressDTO);

    String save(String token, AddressDTO addressDTO);

    String update(String token, Long id);
}
