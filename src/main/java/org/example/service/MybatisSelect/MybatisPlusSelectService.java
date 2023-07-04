package org.example.service.MybatisSelect;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ldd
 */
@Component
public class MybatisPlusSelectService {
    @Autowired
    UserMapper userMapper;
    public User selectOne(){
        User user = userMapper.selectOne(new QueryWrapper<User>().last("limit 1"));
        return user;
    }
}
