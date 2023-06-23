package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("stb")
@Data
public class Stb {
    public String ts;
    public String v1;
    public String t1;
    public String t2;
}
