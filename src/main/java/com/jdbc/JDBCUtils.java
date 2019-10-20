/**
 * Created with IntelliJ IDEA.
 * User: gxm
 * Date: 2019/10/18
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 * Description:  编写重复的操作代码  编写连接池操作list
 **/
package com.jdbc;

import java.sql.*;
import java.util.ArrayList;

public class JDBCUtils {
    private static final String url = "jdbc:mysql://mysql:3306/jdbc_learn";
    private static final String user = "root";
    private static final String password = "123456789";
    private static final int connectionPoolSize = 5;
    private static ArrayList<Connection> arrayList = new ArrayList<Connection>();

    static {//静态代码块先于静态方法执行
        for (int i = 0; i < connectionPoolSize; i++) {       // 初始连接池中加入5条连接
            Connection connection = createConnection();
            arrayList.add(connection);
        }
    }

    public static Connection getConnection() {       //连接使用完时，新建连接
        if (arrayList.isEmpty() == false) {
            Connection connection = arrayList.get(0);
            arrayList.remove(0);
            return connection;
        } else {
            for (int i = 0; i < connectionPoolSize; i++) {       // 为空时 连接池中再扩展加入5条连接
                Connection connection = createConnection();
                arrayList.add(connection);
            }
            return getConnection();
        }
    }

    private static Connection createConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("connection get fail");
            e.printStackTrace();
        }
        return connection;
    }

    public static void close(ResultSet resultSet, Statement statement, Connection connection) {
        //closeConnection(connection);
        arrayList.add(connection);
        closeResultSet(resultSet);
        closeStatement(statement);
    }

    public static void close(Statement statement, Connection connection) {
        closeStatement(statement);
        //closeConnection(connection);
        arrayList.add(connection);
    }

    public static void close(Statement statement1, Statement statement2, Connection connection) {
        closeStatement(statement1);
        closeStatement(statement2);
        //closeConnection(connection);
        arrayList.add(connection);
    }

    private static void closeStatement(Statement statement) {
        try {
            if (statement != null)
                statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void closeConnection(Connection connection) {
/*        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        //归还给连接池
        arrayList.add(connection);
    }

    private static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
