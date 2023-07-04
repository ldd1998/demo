package org.example.redis;

import cn.hutool.core.util.RandomUtil;
import org.example.DemoApplicationForTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest(classes = DemoApplicationForTest.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpeedTest {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    public void test01(){
        stringRedisTemplate.opsForValue().set("key1","value");
        for (int i = 0; i < 20000; i++) {
            String s = RandomUtil.randomNumbers(1);
            stringRedisTemplate.opsForValue().get("key1");
            if(s.equals("1")){
                System.out.println("匹配上");
            }else {
                System.out.println("未匹配上"+s);
            }
        }
    }
}
