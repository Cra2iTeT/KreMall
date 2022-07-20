package com.kdfus.domain.vo.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @date 2022/7/20 10:37
 */
@Data
public class ShoppingCartVO {
    private Long id;

    /**
     * 商品具体id
     */
    private Long goodsId;

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 数量
     */
    private Integer count;
}
