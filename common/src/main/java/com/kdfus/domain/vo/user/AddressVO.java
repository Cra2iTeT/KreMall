package com.kdfus.domain.vo.user;

import lombok.Data;

/**
 * @author Cra2iTeT
 * @date 2022/7/18 16:02
 */
@Data
public class AddressVO {
    private Long id;

    private String name;

    private String phone;

    private Byte isDefault;

    private String province;

    private String city;

    private String region;

    private String detail;

    private Byte isDeleted;
}
