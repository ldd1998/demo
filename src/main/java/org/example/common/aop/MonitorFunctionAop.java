package org.example.common.aop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.mapper.UserMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ldd
 * 监控方法执行时间
 */
@Aspect
@Component
@Slf4j
public class MonitorFunctionAop {
    @Autowired
    UserMapper userMapper;

    @Around("execution(* org.example.service.insertSpeed..*(..))")
    public Object monitorFunctionSpeed(ProceedingJoinPoint point) throws Throwable {
        Integer startCount = userMapper.selectCount(new QueryWrapper<>());
        long startTime = System.currentTimeMillis();
        Object obj = point.proceed();
        long endTime = System.currentTimeMillis();
        Integer endCount = userMapper.selectCount(new QueryWrapper<>());
        double spendTime = (double) (endTime - startTime) / 1000D;
        int countChange = endCount - startCount;
        log.info(point.getTarget().getClass().getSimpleName()+"：耗时：" + spendTime + "秒，更新数据量：" + countChange +"，速率：" + (int)(countChange/spendTime) + "/秒");
        log.info("清除user表。。。");
        userMapper.delete(new QueryWrapper<>());
        return obj;
    }
}
