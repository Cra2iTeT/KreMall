package com.kdfus.domain.dto;

import lombok.Data;

/**
 * @author Cra2iTeT
 * @date 2022/7/15 22:54
 */

@Data
public class UpdatePwdDTO {
    private String oldPasswordMd5;
    private String newPasswordMd5;
    private String confirmPasswordMd5;
}
