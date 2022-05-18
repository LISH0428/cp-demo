package com.example.cpdemo.pool;

import lombok.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Data
public class MyAbstractDataSource implements MyDataSourceInterface{
    private String url;
    private String userName;
    private String password;
    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(userName,password);
    }
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return doConnection(username, password);
    }

    /**
     *
     * @param username 用户名
     * @param password 密码
     * @return 连接
     */
    private Connection doConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
