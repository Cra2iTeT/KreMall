package com.kdfus.domain.entity.mall;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/6/20 20:19
 */

@Data
public class Commodity {
    private Long id;

    private String name;

    /**
     * 商品主图
     */
    private String commodityImg;

    private int stock;

    private int lowestPrice;

    private int highestPrice;

    private Long shopId;

    private Byte isDeleted;

    /**
     * 商品详细
     */
    private String commodityIntro;

    /**
     * 记录该商品有几级规格，例如鞋有两级规格颜色和码数，药物只有一种规格盒数
     */
    private Byte normNum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Long createId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Long updateId;
}
