package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableField(exist = false)
    private String nameTest;
    @TableField(exist = false)
    private String nameTest1;
}
