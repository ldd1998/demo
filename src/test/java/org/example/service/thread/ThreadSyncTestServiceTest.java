package org.example.service.thread;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ThreadSyncTestServiceTest {
    @Autowired
    ThreadSyncTestService threadSyncTestService;
    @Test
    public void syncTest() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("th1");
                threadSyncTestService.getAndInsertUser();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("th2");
                threadSyncTestService.getAndInsertUser();
            }
        }).start();
        Thread.sleep(3000);
    }

    /**
     * 和上面测试共同证明了多线程中回滚不了事务
     */
    @Test
    public void noSyncTest() {
        threadSyncTestService.getAndInsertUser();
    }
}