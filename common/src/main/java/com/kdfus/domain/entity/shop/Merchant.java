package com.kdfus.domain.entity.shop;

import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/6/20 20:16
 */

@Data
public class Merchant {
    private Long id;

    private String accountId;

    private String nickName;

    private String passwordMd5;

    private Byte isDeleted;

    private Long merchantId;

    private Date createTime;
}
