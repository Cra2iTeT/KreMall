package com.kdfus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kdfus.domain.dto.admin.CityDTO;
import com.kdfus.domain.dto.admin.ProvinceDTO;
import com.kdfus.domain.entity.map.City;

import java.util.List;

/**
 * @author Cra2iTeT
 * @date 2022/7/20 20:58
 */
public interface CityService extends IService<City> {

    City getCity(Long id);

    List<City> getCityList();

    Boolean update(Long id, CityDTO cityDTO);

    Boolean delete(Long id, CityDTO cityDTO);

    Boolean save(Long id, CityDTO cityDTO);
}
