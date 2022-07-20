package com.kdfus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdfus.domain.dto.user.ShoppingCartDTO;
import com.kdfus.domain.entity.user.ShoppingCart;
import com.kdfus.domain.vo.user.ShoppingCartVO;
import com.kdfus.domain.vo.user.UserVO;
import com.kdfus.mapper.ShoppingCartMapper;
import com.kdfus.service.ShoppingCartService;
import com.kdfus.system.ServiceResultEnum;
import com.kdfus.util.NumberUtils;
import com.kdfus.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Cra2iTeT
 * @date 2022/7/20 10:10
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public String save(String token, ShoppingCartDTO shoppingCartDTO) {
        UserVO userVO = tokenUtils.verifyUser(token);
        ShoppingCart shoppingCart = BeanUtil.copyProperties(shoppingCartDTO, ShoppingCart.class);
        shoppingCart.setId(NumberUtils.genId());
        shoppingCart.setUserId(userVO.getId());
        if (save(shoppingCart)) {
            return null;
        }
        return ServiceResultEnum.OPERATE_ERROR.getResult();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String update(String token, ShoppingCartDTO shoppingCartDTO) {
        UserVO userVO = tokenUtils.verifyUser(token);
        LambdaUpdateWrapper<ShoppingCart> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(shoppingCartDTO.getCount() != null, ShoppingCart::getCount, shoppingCartDTO.getCount())
                .set(shoppingCartDTO.getGoodsId() != null, ShoppingCart::getGoodsId, shoppingCartDTO.getGoodsId())
                .eq(ShoppingCart::getUserId, userVO.getId()).eq(ShoppingCart::getId, shoppingCartDTO.getId())
                .eq(ShoppingCart::getIsDeleted, (byte) 0).last("for update");

        if (update(wrapper)) {
            return null;
        }
        return ServiceResultEnum.OPERATE_ERROR.getResult();
    }

    // TODO 代码值得优化
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String delete(String token, List<Long> ids) {
        if (removeByIds(ids)) {
            return null;
        }
        return ServiceResultEnum.OPERATE_ERROR.getResult();
    }

    @Override
    public List<ShoppingCartVO> getList(String token) {
        UserVO userVO = tokenUtils.verifyUser(token);
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userVO).eq(ShoppingCart::getIsDeleted, (byte) 0);
        List<ShoppingCart> list = list(wrapper);
        if (list.size() != 0) {
            return list.stream()
                    .map(item -> BeanUtil.copyProperties(item, ShoppingCartVO.class)).toList();
        }
        return null;
    }
}
