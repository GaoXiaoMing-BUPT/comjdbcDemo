/**
 * Created with IntelliJ IDEA.
 * User: gxm
 * Date: 2019/10/18
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 * Description:
 **/
package com.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class DBCPDataSource {
    /* 手动配置 */
/*    private static final String url = "jdbc:mysql://mysql:3306/jdbc_learn";
    private static final String user = "root";
    private static final String password = "123456789";
    private static final int initialSize = 5;
    private static final int maxInitialSize = 20;
    private static final int minIdle = 3;
    private static BasicDataSource dataSource = null;
    static { //初始化dpcp连接池代码
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);//初始五个
        dataSource.setMaxTotal(maxInitialSize);//最大20，即创建数量超过20时等待其他连接释放
        dataSource.setMinIdle(minIdle);        //最小空闲连接，

    }*/

    /* 加载驱动文件 */
    private static BasicDataSource dataSource = null;

    static {
        dataSource = new BasicDataSource();
        InputStream inputStream = DBCPDataSource.class.getClassLoader().getResourceAsStream("dbcp.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            //System.out.println(properties);//测试文件是否读取成功
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        }catch (Exception e) {
            System.out.println("dataSource add properties fail ");
            e.printStackTrace();
            System.exit(1);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean close(Connection connection) {//直接close 但不会真正关闭，会间接的丢入连接池
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean close(Connection connection, PreparedStatement preparedStatement1, PreparedStatement preparedStatement2) {
        try {
            connection.close();
            preparedStatement1.close();
            preparedStatement2.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
