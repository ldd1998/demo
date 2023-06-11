package org.example.service.thread;


import org.example.DemoApplicationForTest;
import org.example.util.SelfTransactionManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = DemoApplicationForTest.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class ThreadSyncTestServiceTest {
    @Autowired
    ThreadSyncTestService threadSyncTestService;
    @Autowired
    private SelfTransactionManager selfTransactionManager;

    @Test
    public void syncTest() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                selfTransactionManager.begin();
                Thread.currentThread().setName("th1");
                threadSyncTestService.getAndInsertUser();
                selfTransactionManager.rollBack();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                selfTransactionManager.begin();
                Thread.currentThread().setName("th2");
                threadSyncTestService.getAndInsertUser();
                selfTransactionManager.rollBack();
            }
        }).start();
        Thread.sleep(3000);
    }

    /**
     * 和上面测试共同证明了多线程中@Transactional回滚不了事务，必须在线程内部手动回滚事务
     */
    @Test
    public void noSyncTest() {
        threadSyncTestService.getAndInsertUser();
    }
}