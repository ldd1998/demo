package org.example.service.tdeninge;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.Stb;
import org.example.mapper.StbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

/**
 * @author ldd
 * TDeninge restful连接测试
 */
@Service
@DS("td")
public class TDeningeService {
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
    public void stbMapperSelect(){
        String tableName = "t1";
        List<Stb> stbs = stbMapper.selectByTable(tableName);
        System.out.println(stbs);
    }
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.taosdata.jdbc.rs.RestfulDriver");
        String jdbcUrl = "jdbc:TAOS-RS://192.168.1.107:6041/test?user=root&password=taosdata";
        Connection conn = DriverManager.getConnection(jdbcUrl);
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("select *,t1,t2 from t1;");
    }
}
