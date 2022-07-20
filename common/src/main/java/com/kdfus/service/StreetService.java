package com.kdfus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kdfus.domain.dto.admin.RegionDTO;
import com.kdfus.domain.dto.admin.StreetDTO;
import com.kdfus.domain.entity.map.Street;

import java.util.List;

/**
 * @author Cra2iTeT
 * @date 2022/7/20 21:00
 */
public interface StreetService extends IService<Street> {

    Street getStreet(Long id);

    List<Street> getStreetList();

    Boolean update(Long id, StreetDTO streetDTO);

    Boolean delete(Long id, StreetDTO streetDTO);

    Boolean save(Long id, StreetDTO streetDTO);
}
