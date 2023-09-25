package org.example.service.forward;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 想通过websocket公用的springboot端口发现不可行
 */

@ServerEndpoint("/database")
public class DatabaseWebSocket {
    private Connection connection;
    
    @OnOpen
    public void onOpen(Session session) {
        try {
            // 建立数据库连接
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.1.107:3306/demos", "root", "liu15853087850");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @OnMessage
    public void onMessage(String query, Session session) {
        try {
            // 执行数据库查询
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            
            // 处理查询结果并发送回客户端
            while (resultSet.next()) {
                String result = resultSet.getString("column_name");
                session.getBasicRemote().sendText(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @OnClose
    public void onClose(Session session) {
        try {
            // 关闭数据库连接
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
}
