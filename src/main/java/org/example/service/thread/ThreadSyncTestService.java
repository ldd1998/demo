package org.example.service.thread;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 测试synchronized作用在方法上时是否有效
 * @author ldd
 */
@Service
@Slf4j
public class ThreadSyncTestService {
    @Autowired
    UserMapper userMapper;
    public void getAndInsertUser(){
        User u = new User();
        u.setId("1");
        User user = userMapper.selectById(1);
        log.info(Thread.currentThread().getName()+"："+user);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(user == null){
            userMapper.insert(u);
            log.info("插入用户1");
        }
    }
}
