package com.kdfus.domain.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/5/31 21:11
 */

/**
 * 用户持久类
 */
@Data
@TableName("tb_mall_user")
public class User {
    private Long id;

    private String nickName;

    /**
     * 账号
     * 默认手机号
     */
    private String accountId;

    private String passwordMd5;

    /**
     * 用户头像
     */
    @TableField(fill = FieldFill.INSERT)
    private String userImg;

    /**
     * 注销标识
     * 0正常，1注销
     */
    @TableField(fill = FieldFill.INSERT)
    private Byte isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
