package org.example.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.example.entity.User;
import org.junit.jupiter.api.Test;

public class Jackson {
    /**
     * 使用jackson，转为下划线明明策略
     * @throws JsonProcessingException
     */
    @Test
    public void test01() throws JsonProcessingException {
        User user = new User();
        user.setAge(1);
        user.setName("ldd");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        String userJson = objectMapper.writeValueAsString(user);
        System.out.println(userJson);
    }
}
