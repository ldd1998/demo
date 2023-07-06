package org.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.stereotype.Component;

/**
 * 1、适用于elasticSearch中驼峰转下划线的jsonString生成工具
 */
@Component
public class ObjectMapperSnakeCaseUtil {
    ObjectMapper objectMapper;
    ObjectMapperSnakeCaseUtil(){
        this.objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    public String toJSONString(Object o) throws JsonProcessingException {
        String oJson = objectMapper.writeValueAsString(o);
        return oJson;
    }

}
