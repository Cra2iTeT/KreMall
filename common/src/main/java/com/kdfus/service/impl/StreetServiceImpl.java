package com.kdfus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdfus.domain.dto.admin.StreetDTO;
import com.kdfus.domain.entity.map.Street;
import com.kdfus.mapper.StreetMapper;
import com.kdfus.service.StreetService;
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
 * @date 2022/7/20 21:00
 */
@Service
@Slf4j
public class StreetServiceImpl extends ServiceImpl<StreetMapper, Street> implements StreetService {

    private static final String STREET_PREFIX = "Map:Street:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取单个街道
     *
     * @param id
     * @return
     */
    public Street getStreet(Long id) {
        String streetJson = stringRedisTemplate.opsForValue().get(STREET_PREFIX + id);
        return JSON.parseObject(streetJson, Street.class);
    }

    /**
     * 获取街道列表
     *
     * @return
     */
    public List<Street> getStreetList() {
        Set<String> keys = redisUtils.fuzzyMatch(STREET_PREFIX + "*");
        List<String> streetJsonList = stringRedisTemplate.opsForValue().multiGet(keys);
        return streetJsonList.stream().map(item -> JSON.parseObject(item, Street.class)).toList();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean update(Long id, StreetDTO streetDTO) {
        LambdaUpdateWrapper<Street> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Street::getUpdateId, id)
                .eq(streetDTO.getName() != null, Street::getName, streetDTO.getName())
                .eq(streetDTO.getIsDeleted() != null, Street::getIsDeleted, streetDTO.getIsDeleted())
                .eq(Street::getId, streetDTO.getId()).last("for update");
        if (update(wrapper)) {
            Street street = getById(streetDTO.getId());
            stringRedisTemplate.opsForValue()
                    .set(STREET_PREFIX + streetDTO.getId(), JSON.toJSONString(street));
            return true;
        }
        return false;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean delete(Long id, StreetDTO streetDTO) {
        LambdaUpdateWrapper<Street> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Street::getIsDeleted, (byte) 1).set(Street::getUpdateId, id)
                .eq(Street::getId, streetDTO.getId()).last("for update");
        if (update(wrapper)) {
            stringRedisTemplate.delete(STREET_PREFIX + streetDTO.getId());
            return true;
        }
        return false;
    }

    @Override
    public Boolean save(Long id, StreetDTO streetDTO) {
        Street street = BeanUtil.copyProperties(streetDTO, Street.class);
        street.setId(NumberUtils.genId());
        street.setCreateId(id);
        street.setUpdateId(id);
        if (save(street)) {
            return true;
        }
        return false;
    }
}
