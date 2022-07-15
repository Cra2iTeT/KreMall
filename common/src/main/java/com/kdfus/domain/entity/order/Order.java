package com.kdfus.domain.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/6/20 20:37
 */

@Data
public class Order {
    private Long id;

    /**
     * 订单编号
     */
    private String no;

    private Long merchantId;

    private Long commodityId;

    private Long goodsId;

    private Long userId;

    private int price;

    private String extraRemark;

    private Byte payStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    private Byte status;

    private Byte isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Long createId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Long updateId;
}
