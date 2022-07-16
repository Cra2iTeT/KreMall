package com.kdfus.config.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.kdfus.system.Constants;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/5/2 20:03
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        // 默认头像地址
        this.setFieldValByName("userImg", Constants.FILE_USER_UPLOAD_DIC + "default.png", metaObject);
        this.setFieldValByName("isDeleted", 0, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
