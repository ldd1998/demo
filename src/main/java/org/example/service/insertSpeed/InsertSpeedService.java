package org.example.service.insertSpeed;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.*;

/**
 * 测试插入Mysql速度
 * @author ldd
 */
@Service
@Slf4j
public class InsertSpeedService {
    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    TransactionDefinition transactionDefinition;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserMapper userMapper;
    /**
     * mybatis-plus 循环插入
     * @param count 插入数量
     */
    public void mybatisInsert(int count, User user){
        for (int i = 0; i < count; i++) {
            userMapper.insert(user);
        }
    }

    /**
     * jdbc 循环插入
     * @param count 数量
     */
    public void jdbcInsert(int count){
        for (int i = 0; i < count; i++) {
            jdbcTemplate.update("INSERT into `user`(name,age,create_time,update_time)VALUES('ldd',20,'2023-01-01 00:00:00','2023-01-01 00:00:00')");
        }
    }

    /**
     * mybatis-plus 循环插入，开启事务
     * @param count 插入数量
     */
    public void mybatisInsertTransaction(int count,User user){
        // 开启事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        for (int i = 0; i < count; i++) {
            userMapper.insert(user);
        }
        // 提交事务
        dataSourceTransactionManager.commit(transactionStatus);
    }
    /**
     * jdbc 循环插入，开启事务
     * @param count 数量
     */
    public void jdbcInsertTransaction(int count){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        jdbcInsert(count);
        dataSourceTransactionManager.commit(transactionStatus);
    }

    /**
     * Mybatis-Plus、多线程、开启事务
     * @param threadCount
     * @param count
     * @throws InterruptedException
     */
    public void mybatisInsertThreadTrans(int threadCount,int count,User user) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadCount,threadCount,1000L, TimeUnit.SECONDS,new LinkedBlockingDeque<>());
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
                    mybatisInsert(count,user);
                    dataSourceTransactionManager.commit(transactionStatus);//提交
                    countDownLatch.countDown();
                }
            };
            threadPoolExecutor.execute(runnable);
        }
        countDownLatch.await();
    }

    /**
     * JDBC、多线程、开启事务
     * @param threadCount
     * @throws InterruptedException
     */
    public void jdbcInsertThreadTrans(int threadCount, List<User> users) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadCount,threadCount,1000L, TimeUnit.SECONDS,new LinkedBlockingDeque<>());
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
                    jdbcTemplate.batchUpdate("INSERT INTO `user` (`id`, `name`, `age`, `create_time`, `update_time`) VALUES" +
                            " (?, ?, ?, ?, ?);", new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setString(1, users.get(i).getId());
                            ps.setString(2, users.get(i).getName());
                            ps.setInt(3, users.get(i).getAge());
                            ps.setString(4, users.get(i).getCreateTime());
                            ps.setString(5, users.get(i).getUpdateTime());
                        }

                        @Override
                        public int getBatchSize() {
                            return users.size();
                        }
                    });
                    dataSourceTransactionManager.commit(transactionStatus);//提交
                    // 这里顺寻不能变，countDownLatch一定要在最后，否则就会未提交就继续执行了
                    countDownLatch.countDown();
                }
            };
            threadPoolExecutor.execute(runnable);
        }
        countDownLatch.await();
    }
}
