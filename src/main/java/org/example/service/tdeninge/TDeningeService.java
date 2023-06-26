package org.example.service.tdeninge;

import cn.hutool.core.date.DateUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Stb;
import org.example.mapper.StbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author ldd
 * TDeninge restful连接测试
 */
@Service
@DS("td")
@Slf4j
public class TDeningeService {
    Statement statement = null;
    public TDeningeService() throws ClassNotFoundException, SQLException {
        Class.forName("com.taosdata.jdbc.rs.RestfulDriver");
        String jdbcUrl = "jdbc:TAOS-RS://192.168.1.107:6041/test?user=root&password=taosdata";
        Connection conn = DriverManager.getConnection(jdbcUrl);
        statement = conn.createStatement();
    }
    @Autowired
    StbMapper stbMapper;
    public void stbMapperSelectList(){
        List<Stb> stbs = stbMapper.selectList(new QueryWrapper<>());
        System.out.println(stbs);
    }
    public void stbMapperInsert(int count){
        for (int i = 0; i < count; i++) {
            String tableName = "t1";
            stbMapper.insertByTable(new Stb(),tableName);
        }
    }
    public void stbMapperInsert02(int count){
        for (int i = 0; i < count; i++) {
            String tableName = "t"+i%100;
            stbMapper.insertByTable(new Stb(),tableName);
        }
    }
    public void stbMapperInsert03(int count) throws ClassNotFoundException, SQLException {
        for (int i = 0; i < count; i++) {
            statement.execute("insert into t2 using stb tags(1,'tag2') values(now,2);");
        }
    }
    public void stbMapperInsert04(int count) throws ClassNotFoundException, SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into t2 using stb tags(1,'tag2') values");
        for (int i = 0; i < count; i++) {
            sql.append("(now,2)");
        }
        statement.execute(sql.toString());
    }
    public void stbMapperInsert05(int count) throws ClassNotFoundException, SQLException {
        for (int i = 0; i < count; i++) {
            String tableName = "t"+i%100;
            statement.addBatch("insert into "+"t"+i%100+" using stb tags(1,'tag2') values (now,2)");
        }
        statement.executeBatch();
    }
    public void stbMapperInsert06(int count) throws ClassNotFoundException, SQLException {
        for (int i = 0; i < count; i++) {
            statement.addBatch("insert into "+"t"+i%1000+" using stb tags(1,'tag2') values (now,2)");
        }
        statement.executeBatch();
    }

    public void stbMapperInsert07(String tableName,int tableCount) throws ClassNotFoundException, SQLException {
        for (int i = 0; i < tableCount; i++) {
            String sql = "insert into "+tableName+" using stb tags(1,'tag2') values (now,2);";
            int status = statement.executeUpdate(sql);
            log.info(sql+"---"+status);
        }
    }

    public void stbMapperInsert08(String tableName,int tableCount) throws ClassNotFoundException, SQLException {
        Class.forName("com.taosdata.jdbc.rs.RestfulDriver");
        String jdbcUrl = "jdbc:TAOS-RS://192.168.1.107:6041/test?user=root&password=taosdata";
        Connection conn = DriverManager.getConnection(jdbcUrl);
        statement = conn.createStatement();

        long currentTimeMillis = System.currentTimeMillis();
        for (long i = 0; i < tableCount; i++) {
            log.info("insert into "+tableName+" using stb tags(1,'tag2') values ("+ currentTimeMillis+i +",2);");
            long time = currentTimeMillis+i;
            statement.addBatch("insert into "+tableName+" using stb tags(1,'tag2') values ("+ time +",2);");
        }
        statement.executeBatch();
//        log.info("status："+ Arrays.asList(ints));
    }
    public void dropAllTables() throws SQLException {
        ResultSet resultSet = statement.executeQuery("show tables");
        while (resultSet.next()){
            String tableName = resultSet.getString(1);
            statement.execute("DROP TABLE "+tableName);
            System.out.println("删除："+tableName);
        }
    }
    public void stbMapperSelect(){
        String tableName = "t1";
        List<Stb> stbs = stbMapper.selectByTable(tableName);
        System.out.println(stbs);
    }
}
