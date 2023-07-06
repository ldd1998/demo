package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.example.config.MyDateTime;

import java.time.LocalDateTime;

/**
 * @author ldd
 */
@Data
@TableName(value = "user")
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    private String name;
    private int age;
    @TableField(fill = FieldFill.INSERT)
    @JsonSerialize(using = MyDateTime.LocalDateTimeSerializer.class)
    private LocalDateTime createTime;
    @JsonSerialize(using = MyDateTime.LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;
}
