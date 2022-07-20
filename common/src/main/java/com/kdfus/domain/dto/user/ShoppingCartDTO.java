package com.kdfus.domain.dto.user;

import lombok.Data;

/**
 * @author Cra2iTeT
 * @date 2022/7/20 10:04
 */
@Data
public class ShoppingCartDTO {
    private Long id;

    private Long goodsId;

    private Long merchantId;

    private Long commodityId;

    private Integer count;

    private Byte isDeleted;
}
