package org.example.common.aop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class MonitorFunctionAop {
    @Autowired
    UserMapper userMapper;

    @Around("execution(* com.example.mybatisplusdemo.service..*(..))")
    public Object monitorFunctionSpeed(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        Integer startCount = userMapper.selectCount(new QueryWrapper<>());
        Object obj = point.proceed();
        Integer endCount = userMapper.selectCount(new QueryWrapper<>());
        long endTime = System.currentTimeMillis();
        double spendTime = Double.valueOf(endTime - startTime) / 1000D;
        int countChange = endCount - startCount;
        System.out.println(point.getTarget().getClass().getName()+"：耗时：" + spendTime + "秒，更新数据量：" + countChange +"，速率：" + (int)(countChange/spendTime) + "/秒");
        return obj;
    }
}
