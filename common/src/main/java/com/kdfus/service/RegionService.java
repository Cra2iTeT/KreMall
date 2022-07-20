package com.kdfus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kdfus.domain.dto.admin.CityDTO;
import com.kdfus.domain.dto.admin.RegionDTO;
import com.kdfus.domain.entity.map.Region;

import java.util.List;

/**
 * @author Cra2iTeT
 * @date 2022/7/20 20:59
 */
public interface RegionService extends IService<Region> {

    Region getRegion(Long id);

    List<Region> getRegionList();

    Boolean update(Long id, RegionDTO regionDTO);

    Boolean delete(Long id, RegionDTO regionDTO);

    Boolean save(Long id, RegionDTO regionDTO);
}
