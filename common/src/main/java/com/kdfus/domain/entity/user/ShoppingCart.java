package com.kdfus.domain.entity.user;

/**
 * @author Cra2iTeT
 * @date 2022/5/31 21:09
 */

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * 购物车持久类
 */

@Data
public class ShoppingCart {
    private Long id;

    private Long userId;

    private Long goodsId;

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 商品id
     */
    private Long commodityId;

    /**
     * 数量
     */
    private Integer count;

    private Byte isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
