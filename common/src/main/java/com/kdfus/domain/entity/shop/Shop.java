package com.kdfus.domain.entity.shop;

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

    private String merchantImg;

    private Byte isDeleted;

    private Long ownerId;

    private Integer fansNum;

    private String merchantLocation;

    private Integer goodsScore;

    private Integer serviceScore;

    private Integer logisticsScore;

    private Date createTime;

    private Date updateTime;
}
