package com.kdfus.domain.entity.commodity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/6/20 20:32
 */

/**
 * 具体规格的单品
 * 例如80码红色的ANTA Cra2iTeT签名战靴
 */
@Data
public class goods {
    private Long id;

    private Long commodityId;

    /**
     * 单品图片
     */
    private String goodsImg;

    private Integer price;

    /**
     * 规格一
     */
    private Long normOne;

    /**
     * 规格二
     */
    private Long normTwo;

    private Byte isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Long createId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Long updateId;
}
