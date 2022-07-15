package com.kdfus.domain.entity.commodity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/6/20 20:27
 */

@Data
public class Category {
    private Long id;

    private Byte level;

    /**
     * 父级id
     * 默认0
     */
    private Long parentId;

    private String name;

    private Byte rank;

    private Byte isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Long createId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Long updateId;
}
