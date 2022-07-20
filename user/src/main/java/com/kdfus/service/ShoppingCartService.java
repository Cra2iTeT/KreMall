package com.kdfus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kdfus.domain.dto.user.ShoppingCartDTO;
import com.kdfus.domain.entity.user.ShoppingCart;
import com.kdfus.domain.vo.user.ShoppingCartVO;

import java.util.List;

/**
 * @author Cra2iTeT
 * @date 2022/7/20 10:10
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    String save(String token, ShoppingCartDTO shoppingCartDTO);

    String update(String token, ShoppingCartDTO shoppingCartDTO);

    String delete(String token, List<Long> ids);

    List<ShoppingCartVO> getList(String token);
}
