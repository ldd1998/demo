package org.example.config;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动增加创建时间更新时间
 * 配合@TableField(fill = FieldFill.INSERT)使用
 * @author ldd
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", DateUtil.formatDateTime(new Date()),metaObject);
        this.setFieldValByName("updateTime", DateUtil.formatDateTime(new Date()),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", DateUtil.formatDateTime(new Date()),metaObject);
    }
}
