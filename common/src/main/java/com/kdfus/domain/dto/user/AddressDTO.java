package com.kdfus.domain.dto.user;

import lombok.Data;

/**
 * @author Cra2iTeT
 * @date 2022/7/18 16:02
 */
@Data
public class AddressDTO {
    private Long id;

    private String name;

    private String phone;

    private Byte isDefault;

    private Long provinceId;

    private Long cityId;

    private Long regionId;

    private Long streetId;

    private String detail;

    private Byte isDeleted;
}
