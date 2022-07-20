package com.kdfus.domain.entity.mall;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @date 2022/6/20 20:37
 */

@Data
public class Order {
    private Long id;

    /**
     * 订单编号
     */
    private String no;

    private Long shopId;

    private Long addressId;

    private Long goodsId;

    private Long userId;

    private int price;

    private String extraRemark;

    private Byte payStatus;

    private Date payTime;

    private Byte status;

    private Byte isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Long createId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Long updateId;
}
