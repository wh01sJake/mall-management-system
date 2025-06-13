package com.intelijake.mall.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * ClassName: MyMetaObjectHandler
 * Description:
 * <p>
 * Datetime: 11/06/2025 19:04
 * Author: @Likun.Fang
 * Version: 1.0
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // 插入时，创建时间和修改时间
        if (metaObject.hasGetter("createTime")) {
            this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        }
        if (metaObject.hasGetter("updateTime")) {
            this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasGetter("updateTime")) {
            this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }
    }
}