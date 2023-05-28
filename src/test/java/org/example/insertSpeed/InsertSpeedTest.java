package org.example.insertSpeed;

import cn.hutool.core.date.DateUtil;
import org.example.DemoApplicationForTest;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.insertSpeed.InsertSpeedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 插入数据库速度测试
 * 测试发现在循环执行的情况下，使用jdbc.update比batchUpdate快近一倍
 * jdbc下开启事务比不开启事务快50%
 * mybatis下开启事务和不开启事务无明显差别
 * 问题1：TODO
 * 在这里启动会出现spring-boot-admin页面打不开的情况，加上随机端口配置
 * （webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT）就可以了，不知道什么原因
 *  或者指定端口（webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT）就好了
 */

@SpringBootTest(classes = DemoApplicationForTest.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@Transactional
public class InsertSpeedTest {
    static User user = new User();
    static {
        // 创建用户对象
        user.setName("ldd");
        user.setAge(20);
        user.setUpdateTime(DateUtil.now());
        user.setCreateTime(DateUtil.now());
    }
    // 插入数据条数
    int count = 10000;
    // 在多线程的情况下的线程数
    int threadCount = 10;
    @Autowired
    UserMapper userMapper;
    @Autowired
    InsertSpeedService insertSpeedService;

//    @MockBean
//    SpringBootAdminTask springBootAdminTask;
    /**
     * mybatis-plus循环插入
     * 耗时：11.096秒，更新数据量：10000，速率：901/秒
     */
    @Test
    public void mybatisInsert(){
        insertSpeedService.mybatisInsert(count,user);
    }

    /**
     * jdbc循环插入
     * InsertSpeedService：耗时：9.317秒，更新数据量：10000，速率：1073/秒
     * 使用jdbc时aop没有拿到更新数据不知原因 TODO,但是可以正常回滚
     */
    @Test
    public void jdbcInsert(){
        insertSpeedService.jdbcInsert(count);
    }

    /**
     * mybatis-plus循环插入，开启事务
     * 耗时：11.15秒，更新数据量：10000，速率：896/秒
     */
    @Test
    public void mybatisInsertTransaction(){
        insertSpeedService.mybatisInsertTransaction(count,user);
    }

    /**
     * jdbc循环插入，开启事务
     * 耗时：6.644秒，更新数据量：10000，速率：1505/秒
     */
    @Test
    public void jdbcInsertTransaction(){
        insertSpeedService.jdbcInsertTransaction(count);
    }

    /**
     * Mybatis-Plus，10个线程，开启事务
     * 耗时：1.251秒，更新数据量：10000，速率：7993/秒
     */
    @Test
    public void mybatisInsertThreadTrans() throws InterruptedException {
        insertSpeedService.mybatisInsertThreadTrans(threadCount,count/threadCount,user);
    }

    /**
     * jdbc、10个线程、开启事务、使用预处理
     * 耗时：0.711秒，更新数据量：10000，速率：14064/秒
     */
    @Test
    public void jdbcInsertThreadTrans() throws InterruptedException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count/threadCount; i++) {
            users.add(user);
        }
        insertSpeedService.jdbcInsertThreadTrans(threadCount,users);
    }

}
