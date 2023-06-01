package org.example.check;

import java.sql.*;

public class DatabaseTableFetcher {
    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.193.101:3306/demos?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8"; // 数据库连接URL
        String username = "root"; // 数据库用户名
        String password = "liu15853087850"; // 数据库密码
        String schema = "demos"; // 指定的 schema 名称


        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String catalog = connection.getCatalog(); // 获取当前数据库的名称

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(catalog, null, null, new String[]{"TABLE"});

            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println("Table Name: " + tableName);
                // 可以根据需要获取其他表信息
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
