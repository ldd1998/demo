package org.example.elasticsearch;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ldd
 * elasticsearch操作示例
 */
@Service
public class ElasticsearchService {
    @Autowired
    UserMapper userMapper;

    /**
     * 执行sql
     */
    @DS("es")
    public void selectListEs(){
        List<User> users = userMapper.selectList(new QueryWrapper<>());
        System.out.println(users);
    }
}
