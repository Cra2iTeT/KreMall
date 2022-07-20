package com.kdfus.domain.entity.shop;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/6/20 20:12
 */

@Data
public class Shop {
    private Long id;

    private String name;

    private String logo;

    private Byte isDeleted;

    private Long ownerId;

    private Integer fansNum;

    private Long shopProvinceId;

    private Long shopCityId;

    private Integer goodsScore;

    private Integer serviceScore;

    private Integer logisticsScore;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
