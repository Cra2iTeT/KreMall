package com.kdfus.domain.dto;

import lombok.Data;

/**
 * @author Cra2iTeT
 * @date 2022/6/29 14:51
 */
@Data
public class RegistryDTO {
    private String accountId;

    private String passwordMd5;

    private String confirmPasswordMd5;
}
