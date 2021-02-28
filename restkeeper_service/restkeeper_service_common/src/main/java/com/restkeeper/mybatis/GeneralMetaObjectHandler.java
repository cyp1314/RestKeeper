package com.restkeeper.mybatis;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: 通用字段处理
 * @date 2021-02-28 16:21
 */

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GeneralMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            setFieldValByName("lastUpdateTime", LocalDateTime.now(),metaObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        insertFill(metaObject);
    }
}
