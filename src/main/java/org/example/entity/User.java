package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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
    private String createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;
    @TableField(exist = false)
    private String nameTest;
    private String nameTest1;

}
