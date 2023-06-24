package org.example.service.tdeninge;

import org.example.DemoApplicationForTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

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
     * 27w/秒，有点快了，但是发现数据库中只有几十条，原来时间戳不能重复。
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Test
    public void TestInsert04() throws SQLException, ClassNotFoundException {
        tDeningeService.stbMapperInsert04(100000);
    }

    /**
     * 100000条耗时114秒
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Test
    public void stbMapperInsert05() throws SQLException, ClassNotFoundException {
        tDeningeService.stbMapperInsert05(100000);
    }

    /**
     * 分成1000张表也是100000条耗时114秒
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Test
    public void stbMapperInsert06() throws SQLException, ClassNotFoundException {
        tDeningeService.stbMapperInsert06(100000);
    }

    @Test
    public void Test2() {
        tDeningeService.stbMapperSelect();
    }
}
