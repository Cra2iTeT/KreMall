package com.kdfus.domain.entity.map;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @date 2022/7/20 20:09
 */
@Data
public class Street {
    private Long id;

    private String name;

    private Byte isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Long createId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;
}
