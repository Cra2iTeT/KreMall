package com.kdfus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kdfus.domain.dto.admin.ProvinceDTO;
import com.kdfus.domain.entity.map.Province;

import java.util.List;

/**
 * @author Cra2iTeT
 * @date 2022/7/20 20:57
 */
public interface ProvinceService extends IService<Province> {
    Province getProvince(Long id);

    List<Province> getProvinceList();

    Boolean update(Long id, ProvinceDTO provinceDTO);

    Boolean delete(Long id, ProvinceDTO provinceDTO);

    Boolean save(Long id, ProvinceDTO provinceDTO);
}
