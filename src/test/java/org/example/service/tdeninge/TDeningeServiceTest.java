package org.example.service.tdeninge;

import cn.hutool.core.thread.ThreadUtil;
import lombok.SneakyThrows;
import org.example.DemoApplicationForTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

@SpringBootTest(classes = DemoApplicationForTest.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TDeningeServiceTest {
    @Autowired
    TDeningeService tDeningeService;

    @Test
    public void Test() {
        tDeningeService.stbMapperSelectList();
    }

    /**
     * 插入100000条数据用时109秒
     * 917/s，好慢
     */
    @Test
    public void TestInsert01() {
        tDeningeService.stbMapperInsert(10000);
    }

    /**
     * 分别插入到100张子表，共10000条数据用时23秒
     * 430/s
     */
    @Test
    public void TestInsert02() {
        tDeningeService.stbMapperInsert02(10000);
    }

    /**
     * 插入10000条数据，14秒
     * 714/s
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Test
    public void TestInsert03() throws SQLException, ClassNotFoundException {
        tDeningeService.stbMapperInsert03(10000);
    }

    /**
     * 100000条，317ms
     * 27w/秒，有点快了，但是发现数据库中只有几十条，原来时间戳不能重复。新记录将被直接抛弃
     * 因此理论上限就是一张表1000/s
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Test
    public void TestInsert04() throws SQLException, ClassNotFoundException {
        tDeningeService.stbMapperInsert04(100000);
    }

    /**
     * 100000条耗时114秒
     * 877/s
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Test
    public void stbMapperInsert05() throws SQLException, ClassNotFoundException {
        tDeningeService.stbMapperInsert05(100000);
    }

    /**
     * 分成1000张表也是100000条耗时114秒
     * 877/s
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Test
    public void stbMapperInsert06() throws SQLException, ClassNotFoundException {
        tDeningeService.stbMapperInsert06(100000);
    }

    /**
     * 多线程插入，每个线程一个表
     * 100张表，100个线程，每个表1000条数据，10w耗时18秒
     * cpu 88%，网络发送，接收均为18Mbps/s
     * 5555/s 也不快呀，cpu和网络达到这么多
     * 理论上100张表，每张1000/s，应该是10w/s
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Test
    public void stbMapperInsert07() throws SQLException, ClassNotFoundException, InterruptedException {
        int tableCount = 1000;
        int tableThreadCount = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(tableThreadCount);
        for (int t = 0; t < tableThreadCount; t++) {
            int tName = t;
            ThreadUtil.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    tDeningeService.stbMapperInsert07("t"+tName,tableCount);
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
    }

    /**
     * 时间不采用now而是自定义
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    @Test
    public void stbMapperInsert08() throws SQLException, ClassNotFoundException, InterruptedException {
        int tableCount = 10000;
        int tableThreadCount = 2;
        CountDownLatch countDownLatch = new CountDownLatch(tableThreadCount);
        for (int t = 0; t < tableThreadCount; t++) {
            int tName = t;
            ThreadUtil.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    tDeningeService.stbMapperInsert08("t"+tName,tableCount);
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
    }

    /**
     * 删除所有子表
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Test
    public void dropAllTables() throws SQLException, ClassNotFoundException {
        tDeningeService.dropAllTables();
    }

    @Test
    public void Test2() {
        tDeningeService.stbMapperSelect();
    }
}
