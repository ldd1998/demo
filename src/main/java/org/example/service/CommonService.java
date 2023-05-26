package org.example.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ldd
 */
@Service
public class CommonService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;
    public void threadInsertUser(){
        long start = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    DefaultTransactionDefinition df = new DefaultTransactionDefinition();
                    df.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                    TransactionStatus transaction = dataSourceTransactionManager.getTransaction(df);
                    for (int i = 0; i < 1000; i++) {
                        String simpleUUID = IdUtil.simpleUUID();
                        User user = new User();
                        user.setId(simpleUUID);
                        user.setName("ldd");
                        user.setAge(20);
                        userMapper.insert(user);
                    }
                    dataSourceTransactionManager.commit(transaction);
                }
            }));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start)/100L);
    }

    public void insertUser(){
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String simpleUUID = IdUtil.simpleUUID();
            User user = new User();
            user.setId(simpleUUID);
            user.setName("ldd");
            user.setAge(20);
            userMapper.insert(user);
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start)/1000L);
    }

    public void jdbcInsert() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            jdbcTemplate.execute("INSERT INTO `user` ('id', 'name', 'age', 'create_time', 'update_time') VALUES" +
                    " ('0000507d42e042bca735943016fa2750', 'ldd', 20, '2023-02-14 19:41:18', '2023-02-14 19:41:18');");
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start)/1000L);
    }


    public void jdbcInsetThread(){
        long start = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        jdbcTemplate.execute("INSERT INTO `user` ('id', 'name', 'age', 'create_time', 'update_time') VALUES" +
                                " ('0000507d42e042bca735943016fa2750', 'ldd', 20, '2023-02-14 19:41:18', '2023-02-14 19:41:18');");
                    }
                }
            }));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start)/1000L);
    }

    public void jdbcInsetThreadTrans(){
        long start = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    DefaultTransactionDefinition df = new DefaultTransactionDefinition();
                    df.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                    TransactionStatus transaction = dataSourceTransactionManager.getTransaction(df);
                    for (int i = 0; i < 10000; i++) {
                        jdbcTemplate.execute("INSERT INTO 'user' ('id', 'name', 'age', 'create_time', 'update_time') VALUES" +
                                " ('0000507d42e042bca735943016fa2750', 'ldd', 20, '2023-02-14 19:41:18', '2023-02-14 19:41:18');");
                    }
                    dataSourceTransactionManager.commit(transaction);
                }
            }));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start)/1000L);
    }
    public void jdbcBatchInsert(){
        long start = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    DefaultTransactionDefinition df = new DefaultTransactionDefinition();
                    df.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                    TransactionStatus transaction = dataSourceTransactionManager.getTransaction(df);
                    jdbcTemplate.batchUpdate("INSERT INTO 'user' ('id', 'name', 'age', 'create_time', 'update_time') VALUES" +
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
                            return 100000;
                        }
                    });
                    dataSourceTransactionManager.commit(transaction);
                }
            }));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long end = System.currentTimeMillis();
        System.out.println((end - start)/100L);
    }

    public List<User> selectUserByPage(int page,int size) {
        IPage iPage = new Page(page,size);
        List<User> users = userMapper.selectUserByPage(iPage);
        return users;
    }
}
