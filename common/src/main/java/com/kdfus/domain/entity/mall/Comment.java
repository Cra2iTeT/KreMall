package com.kdfus.domain.entity.mall;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @date 2022/6/20 20:29
 */

@Data
public class Comment {
    private Long id;

    private Long userId;

    private Long shopId;

    private Long commodityId;

    private Long goodsId;

    private String comment;

    private Byte isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
