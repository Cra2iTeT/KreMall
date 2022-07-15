package com.kdfus.domain.entity.admin;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @date 2022/6/20 20:44
 */

@Data
@TableName("tb_mall_admin")
public class Admin {
    private Long id;

    private String accountId;

    private String passwordMd5;

    private String nickName;

    private String adminImg;

    @TableField(fill = FieldFill.INSERT)
    private Byte isDeleted;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
