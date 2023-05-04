package org.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

/**
 * 手动事务管理工具
 */
@Component
public class SelfTransactionManager {
    private TransactionStatus transactionStatus;
    //获取事务源
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
 
    @Autowired
    private TransactionDefinition transactionDefinition;
    /**
     * 手动开启事务
     */
    public TransactionStatus begin() {
        transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        return transactionStatus;
    }
 
    /**
     * 提交事务
     */
    public void commit(TransactionStatus transactionStatus) {
        platformTransactionManager.commit(transactionStatus);
    }
 
    /**
     * 回滚事务
     */
    public void rollBack() {
        platformTransactionManager.rollback(transactionStatus);
    }
}