package com.kdfus.domain.entity.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @date 2022/5/31 21:13
 */

/**
 * 用户收货地址持久类
 */
@Data
public class Address {
    private Long id;

    private Long userId;

    private String name;

    private String phone;

    /**
     * 是否默认标识
     * 0非默认，1默认
     */
    private Byte isDefault;

    private Long provinceId;

    private Long cityId;

    private Long regionId;

    private Long streetId;

    private String detail;

    /**
     * 删除标识
     * 0未删除，1删除
     */
    private Byte isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
