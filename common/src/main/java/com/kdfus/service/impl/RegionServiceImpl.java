package com.kdfus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdfus.domain.dto.admin.RegionDTO;
import com.kdfus.domain.entity.map.Region;
import com.kdfus.mapper.RegionMapper;
import com.kdfus.service.RegionService;
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
 * @date 2022/7/20 20:59
 */
@Service
@Slf4j
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    private static final String REGION_PREFIX = "Map:Region:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取单个地区
     *
     * @param id
     * @return
     */
    public Region getRegion(Long id) {
        String regionJson = stringRedisTemplate.opsForValue().get(REGION_PREFIX + id);
        return JSON.parseObject(regionJson, Region.class);
    }

    /**
     * 获取地区列表
     *
     * @return
     */
    public List<Region> getRegionList() {
        Set<String> keys = redisUtils.fuzzyMatch(REGION_PREFIX + "*");
        List<String> regionJsonList = stringRedisTemplate.opsForValue().multiGet(keys);
        return regionJsonList.stream().map(item -> JSON.parseObject(item, Region.class)).toList();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean update(Long id, RegionDTO regionDTO) {
        LambdaUpdateWrapper<Region> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Region::getUpdateId, id)
                .eq(regionDTO.getName() != null, Region::getName, regionDTO.getName())
                .eq(regionDTO.getIsDeleted() != null, Region::getIsDeleted, regionDTO.getIsDeleted())
                .eq(Region::getId, regionDTO.getId()).last("for update");
        if (update(wrapper)) {
            Region region = getById(regionDTO.getId());
            stringRedisTemplate.opsForValue()
                    .set(REGION_PREFIX + regionDTO.getId(), JSON.toJSONString(region));
            return true;
        }
        return false;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean delete(Long id, RegionDTO regionDTO) {
        LambdaUpdateWrapper<Region> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Region::getIsDeleted, (byte) 1).set(Region::getUpdateId, id)
                .eq(Region::getId, regionDTO.getId()).last("for update");
        if (update(wrapper)) {
            stringRedisTemplate.delete(REGION_PREFIX + regionDTO.getId());
            return true;
        }
        return false;
    }

    @Override
    public Boolean save(Long id, RegionDTO regionDTO) {
        Region region = BeanUtil.copyProperties(regionDTO, Region.class);
        region.setId(NumberUtils.genId());
        region.setCreateId(id);
        region.setUpdateId(id);
        if (save(region)) {
            return true;
        }
        return false;
    }
}
