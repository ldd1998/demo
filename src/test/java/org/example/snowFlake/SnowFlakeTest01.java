package org.example.snowFlake;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;

public class SnowFlakeTest01 {
    /**
     * 测试雪花算法是自增的
     */
    @Test
    public void testSnowFlake(){
        long l1 = System.currentTimeMillis();
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        Snowflake snowflake1 = IdUtil.getSnowflake(1, 1);
        for (int i = 0; i < 1000; i++) {
            long l = snowflake.nextId();
            long l2 = snowflake1.nextId();
            System.out.println("1="+l);
            System.out.println("2="+l2);
        }
        long l2 = System.currentTimeMillis();
        System.out.println(l2 - l1);
    }
}
