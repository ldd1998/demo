package org.example.service.insertMysqlService;

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
import java.util.concurrent.*;

/**
 * 测试插入Mysql速度
 * @author ldd
 */
@Service
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
     * mybatis 单线程普通插入
     * @param count 插入数量
     */
    public void mybatisInsert(int count){
        User user = new User();
        for (int i = 0; i < count; i++) {
            userMapper.insert(user);
        }
    }

    /**
     * 多线程
     * @param threadCount
     * @param count
     * @throws InterruptedException
     */
    public void mybatisInsertThread(int threadCount,int count) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadCount,threadCount,1000L, TimeUnit.SECONDS,new LinkedBlockingDeque<>());
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    User user = new User();
                    for (int i = 0; i < count; i++) {
                        userMapper.insert(user);
                    }
                    countDownLatch.countDown();
                }
            });
            threadPoolExecutor.execute(thread);
        }
        countDownLatch.await();
    }

    /**
     * 多线程、开启事务
     * @param threadCount
     * @param count
     * @throws InterruptedException
     */
    public void mybatisInsertThreadTrans(int threadCount,int count) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadCount,threadCount,1000L, TimeUnit.SECONDS,new LinkedBlockingDeque<>());
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    User user = new User();
                    TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
                    for (int i = 0; i < count; i++) {
                        userMapper.insert(user);
                    }
                    dataSourceTransactionManager.commit(transactionStatus);//提交
                    countDownLatch.countDown();
                }
            });
            threadPoolExecutor.execute(thread);
        }
        countDownLatch.await();
    }

    /**
     * JDBC、多线程、开启事务
     * @param threadCount
     * @param count
     * @throws InterruptedException
     */
    public void jdbcInsertThreadTrans(int threadCount,int count) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadCount,threadCount,1000L, TimeUnit.SECONDS,new LinkedBlockingDeque<>());
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
                    jdbcTemplate.batchUpdate("INSERT INTO `user` (`id`, `name`, `age`, `create_time`, `update_time`) VALUES" +
                            " (?, ?, ?, ?, ?);",new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setString(1,"0000507d42e042bca735943016fa2750");
                            ps.setString(2, "ldd");
                            ps.setInt(3, 20);
                            ps.setString(4, "2023-02-14 19:41:18");
                            ps.setString(5, "2023-02-14 19:41:18");
                        }
                        @Override
                        public int getBatchSize() {
                            return count;
                        }
                    });
                    countDownLatch.countDown();
                    dataSourceTransactionManager.commit(transactionStatus);//提交
                }
            });
            threadPoolExecutor.execute(thread);
        }
        countDownLatch.await();
    }

    /**
     * JDBC、多线程、开启事务
     * @param threadCount
     * @param count
     * @throws InterruptedException
     */
    public void jdbcInsertUser2ThreadTrans(int threadCount,int count) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadCount,threadCount,1000L, TimeUnit.SECONDS,new LinkedBlockingDeque<>());
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
                    jdbcTemplate.batchUpdate("INSERT INTO `user2` (`id`, `name`, `age`, `create_time`, `update_time`) VALUES" +
                            " (?, ?, ?, ?, ?);",new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setString(1,"0000507d42e042bca735943016fa2750");
                            ps.setString(2, "ldd");
                            ps.setInt(3, 20);
                            ps.setString(4, "2023-02-14 19:41:18");
                            ps.setString(5, "2023-02-14 19:41:18");
                        }
                        @Override
                        public int getBatchSize() {
                            return count;
                        }
                    });
                    countDownLatch.countDown();
                    dataSourceTransactionManager.commit(transactionStatus);//提交
                }
            });
            threadPoolExecutor.execute(thread);
        }
        countDownLatch.await();
    }
}
