package com.kdfus.domain.dto.admin;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @date 2022/7/20 20:48
 */
@Data
public class ProvinceDTO {
    private Long id;

    private String name;

    private Byte isDeleted;
}
