package org.example.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;
import java.util.Map;


public class JdbcConnectDbTest {
    /**
     * 使用jdbctemplate的方式手动连接数据库
     */
    @Test
    public void connectDbTest(){
        // 创建数据源对象
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.1.107:3306/demos");
        dataSource.setUsername("root");
        dataSource.setPassword("liu15853087850");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from demos.user limit 2");
        System.out.println(maps);
    }
}
