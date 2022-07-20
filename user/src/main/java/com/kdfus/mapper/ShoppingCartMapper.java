package com.kdfus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kdfus.domain.entity.user.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Cra2iTeT
 * @date 2022/7/20 10:10
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
