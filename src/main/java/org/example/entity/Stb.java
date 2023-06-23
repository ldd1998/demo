package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("stb")
@Data
public class Stb {
    public Date ts;
    public String v1;
    public String t1;
    public String t2;
}
