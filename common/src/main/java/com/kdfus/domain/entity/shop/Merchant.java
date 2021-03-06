package com.kdfus.domain.entity.shop;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @date 2022/6/20 20:16
 */

@Data
public class Merchant {
    private Long id;

    private String accountId;

    private String nickName;

    private String passwordMd5;

    private Byte isDeleted;

    private Long shopId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
