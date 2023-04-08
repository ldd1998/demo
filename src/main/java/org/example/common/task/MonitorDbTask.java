package org.example.common.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.mapper.UserMapper;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ldd
 */
@Service
@Aspect
public class MonitorDbTask {
    @Autowired
    UserMapper userMapper;
    Integer lastCount = 0;
//    @Scheduled(fixedDelay = 1000)
    void monitorDbChangeSpeed(){
        Integer count = userMapper.selectCount(new QueryWrapper<>());
        int changePerSec = count - lastCount;
        lastCount = count;
        System.out.println("数据变化" + changePerSec+"/秒" + "当前数据：" +count);
    }

}
