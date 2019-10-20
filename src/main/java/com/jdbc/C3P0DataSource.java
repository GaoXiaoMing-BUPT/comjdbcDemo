/**
 * Created with IntelliJ IDEA.
 * User: gxm
 * Date: 2019/10/20
 * Time: 9:56
 * To change this template use File | Settings | File Templates.
 * Description:
 **/
package com.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class C3P0DataSource {
    private static final String url = "jdbc:mysql://mysql:3306/jdbc_learn";
    private static final String user = "root";
    private static final String password = "123456789";
    private static final String mysqlDriverClass = "com.mysql.jdbc.Driver";
    private static final int initialSize = 5;
    private static final int maxInitialSize = 20;
    private static final int minIdle = 3;
    private static ComboPooledDataSource comboPooledDataSource;
    /* 手动配置 */
    static {
        comboPooledDataSource = new ComboPooledDataSource();
        try {
            comboPooledDataSource.setDriverClass(mysqlDriverClass);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        comboPooledDataSource.setJdbcUrl(url);
        comboPooledDataSource.setUser(user);
        comboPooledDataSource.setPassword(password);
        comboPooledDataSource.setInitialPoolSize(initialSize);
        comboPooledDataSource.setMaxPoolSize(maxInitialSize);
        comboPooledDataSource.setMinPoolSize(minIdle);
    }
 /*   *//* 从配置文件读取 *//*
    static {
        comboPooledDataSource = new ComboPooledDataSource();

    }*/
    /*
    *   获取连接
    * */
    public static Connection getConnection(){
        try {
            return comboPooledDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean close(Connection connection, PreparedStatement preparedStatement1,PreparedStatement preparedStatement2){
        try {
            connection.close();
            preparedStatement1.close();
            preparedStatement2.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
