package com.kdfus.domain.entity.mall;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @date 2022/6/20 20:41
 */

@Data
public class OrderGoods {
    private Long id;

    private Long orderId;

    private Long merchantId;

    private Long commodityId;

    private Long goodsId;

    private int count;

    private int price;

    private Byte isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
