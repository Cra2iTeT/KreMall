package com.kdfus.domain.dto.admin;

import lombok.Data;

/**
 * @author Cra2iTeT
 * @date 2022/7/20 20:50
 */
@Data
public class RegionDTO {
    private Long id;

    private String name;

    private Byte isDeleted;
}
