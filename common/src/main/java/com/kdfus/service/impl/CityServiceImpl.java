package com.kdfus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdfus.domain.dto.admin.CityDTO;
import com.kdfus.domain.entity.map.City;
import com.kdfus.mapper.CityMapper;
import com.kdfus.service.CityService;
import com.kdfus.util.NumberUtils;
import com.kdfus.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author Cra2iTeT
 * @date 2022/7/20 20:58
 */
@Service
@Slf4j
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements CityService {

    private static final String CITY_PREFIX = "Map:City:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取单个城市
     *
     * @param id
     * @return
     */
    public City getCity(Long id) {
        String cityJson = stringRedisTemplate.opsForValue().get(CITY_PREFIX + id);
        return JSON.parseObject(cityJson, City.class);
    }

    /**
     * 获取城市列表
     *
     * @return
     */
    public List<City> getCityList() {
        Set<String> keys = redisUtils.fuzzyMatch(CITY_PREFIX + "*");
        List<String> cityJsonList = stringRedisTemplate.opsForValue().multiGet(keys);
        return cityJsonList.stream().map(item -> JSON.parseObject(item, City.class)).toList();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean update(Long id, CityDTO cityDTO) {
        LambdaUpdateWrapper<City> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(City::getUpdateId, id)
                .eq(cityDTO.getName() != null, City::getName, cityDTO.getName())
                .eq(cityDTO.getIsDeleted() != null, City::getIsDeleted, cityDTO.getIsDeleted())
                .eq(City::getId, cityDTO.getId()).last("for update");
        if (update(wrapper)) {
            City city = getById(cityDTO.getId());
            stringRedisTemplate.opsForValue()
                    .set(CITY_PREFIX + cityDTO.getId(), JSON.toJSONString(city));
            return true;
        }
        return false;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean delete(Long id, CityDTO cityDTO) {
        LambdaUpdateWrapper<City> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(City::getIsDeleted, (byte) 1).set(City::getUpdateId, id)
                .eq(City::getId, cityDTO.getId()).last("for update");
        if (update(wrapper)) {
            stringRedisTemplate.delete(CITY_PREFIX + cityDTO.getId());
            return true;
        }
        return false;
    }

    @Override
    public Boolean save(Long id, CityDTO cityDTO) {
        City city = BeanUtil.copyProperties(cityDTO, City.class);
        city.setId(NumberUtils.genId());
        city.setCreateId(id);
        city.setUpdateId(id);
        if (save(city)) {
            return true;
        }
        return false;
    }
}
