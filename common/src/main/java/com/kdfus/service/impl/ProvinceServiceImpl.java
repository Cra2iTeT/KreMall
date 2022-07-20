package com.kdfus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdfus.domain.dto.admin.ProvinceDTO;
import com.kdfus.domain.entity.map.Province;
import com.kdfus.mapper.ProvinceMapper;
import com.kdfus.service.ProvinceService;
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
 * @date 2022/7/20 20:57
 */
@Service
@Slf4j
public class ProvinceServiceImpl extends ServiceImpl<ProvinceMapper, Province> implements ProvinceService {

    private static final String PROVINCE_PREFIX = "Map:Province:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取单个省份
     *
     * @param id
     * @return
     */
    @Override
    public Province getProvince(Long id) {
        String provinceJson = stringRedisTemplate.opsForValue().get(PROVINCE_PREFIX + id);
        return JSON.parseObject(provinceJson, Province.class);
    }

    /**
     * 获取省份列表
     *
     * @return
     */
    @Override
    public List<Province> getProvinceList() {
        Set<String> keys = redisUtils.fuzzyMatch(PROVINCE_PREFIX + "*");
        List<String> provinceJsonList = stringRedisTemplate.opsForValue().multiGet(keys);
        return provinceJsonList.stream().map(item -> JSON.parseObject(item, Province.class)).toList();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean update(Long id, ProvinceDTO provinceDTO) {
        LambdaUpdateWrapper<Province> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Province::getUpdateId, id)
                .eq(provinceDTO.getName() != null, Province::getName, provinceDTO.getName())
                .eq(provinceDTO.getIsDeleted() != null, Province::getIsDeleted, provinceDTO.getIsDeleted())
                .eq(Province::getId, provinceDTO.getId()).last("for update");
        if (update(wrapper)) {
            Province province = getById(provinceDTO.getId());
            stringRedisTemplate.opsForValue()
                    .set(PROVINCE_PREFIX + provinceDTO.getId(), JSON.toJSONString(province));
            return true;
        }
        return false;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Boolean delete(Long id, ProvinceDTO provinceDTO) {
        LambdaUpdateWrapper<Province> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Province::getIsDeleted, (byte) 1).set(Province::getUpdateId, id)
                .eq(Province::getId, provinceDTO.getId()).last("for update");
        if (update(wrapper)) {
            stringRedisTemplate.delete(PROVINCE_PREFIX + provinceDTO.getId());
            return true;
        }
        return false;
    }

    @Override
    public Boolean save(Long id, ProvinceDTO provinceDTO) {
        Province province = BeanUtil.copyProperties(provinceDTO, Province.class);
        province.setId(NumberUtils.genId());
        province.setCreateId(id);
        province.setUpdateId(id);
        if (save(province)) {
            return true;
        }
        return false;
    }
}
