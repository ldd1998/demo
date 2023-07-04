package org.example.json;

import com.alibaba.fastjson.JSONObject;
import org.example.entity.User;
import org.junit.jupiter.api.Test;

/**
 * json操作相关测试类
 */
public class FastJson {

    /**
     * 使用fastjson，驼峰转下划线json，使用注解@JSONField(name = "update_time")，不太方便
     * 这里的问题是值为null的没有转化出来
     * 还有注解@JSONType(naming=PropertyNamingStrategy.SnakeCase)未测试
     */
    @Test
    public void test02(){
        User user = new User();
        user.setAge(1);
        user.setName("ldd");
//        user.setUpdateTime(LocalDateTime.now());
//        user.setCreateTime(LocalDateTime.now());

        String userJson = JSONObject.toJSONString(user);
        System.out.println(userJson);
    }
}
