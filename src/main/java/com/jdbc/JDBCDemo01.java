/**
 * Created with IntelliJ IDEA.
 * User: gxm
 * Date: 2019/10/15
 * Time: 20:54
 * To change this template use File | Settings | File Templates.
 * Description: 练习代码查询及写入数据 比较Statement 和 PreparedStatement 的基本操作
 *              使用自编写连接池及去除sql注入
 **/
package com.jdbc;


import java.sql.*;

public class JDBCDemo01 {
    public static void main(String[] args) {
        /* 产生一万条数据 */
/*        try {
            generateUserData(10000);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        System.out.println("statement:" + loginMysql("username_aaaaaaaaaa", "asad' or 1=1"));//由于sql注入问题 此时返回true
        System.out.println("preparedStatement：" + loginMysqlNone("username_aaaaaaaaaa", "asad"));
        selectByPage(15, 15);
    }

    //sql 含注入
    static boolean loginMysql(String username, String password) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            //此时仅查询 username和password，其余字段不返回
            //String sqlx = "select username,password from student where username='" + username + "'and password='" + password + "'";
            //返回该行所有字段
            String sqlx = "select * from student where username='" + username + "'and password='" + password;
            resultSet = statement.executeQuery(sqlx);
            if (resultSet.next()) {
                JDBCUtils.close(resultSet, statement, connection);
                return true;
            } else {
                JDBCUtils.close(resultSet, statement, connection);
                return false;
            }
/*            if (resultSet.next()) {
                System.out.println("login success");
                System.out.println(resultSet.getString("Id") + ","
                        + resultSet.getString("Name") + ","
                        + resultSet.getString("username") + ","
                        + resultSet.getString("stuNumber") + ","
                        + resultSet.getString("sex") + ","
                        + resultSet.getString("age")
                );
                while (resultSet.next())
                    System.out.println(resultSet.getString("Id") + ","
                            + resultSet.getString("Name") + ","
                            + resultSet.getString("username") + ","
                            + resultSet.getString("stuNumber") + ","
                            + resultSet.getString("sex") + ","
                            + resultSet.getString("age") + ","
                            + "username not unique"
                    );

            } else
                System.out.println("login error user not exist");*/

        } catch (SQLException e) {
            System.out.println("获取连接失败");
            e.printStackTrace();
        } finally {

            JDBCUtils.close(resultSet, statement, connection);

        }
        return false;
    }

    //去除sql注入
    static boolean loginMysqlNone(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sqlx = "select username,password from jdbc_learn.student where username = ? and password = ?";
            preparedStatement = connection.prepareStatement(sqlx);
            /* 此处解决sql注入问题 */
            preparedStatement.setString(1, username);//设置第一个和第二个 参数 即问号？所代表的字符串
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                JDBCUtils.close(resultSet, preparedStatement, connection);
                return true;
            } else {
                JDBCUtils.close(resultSet, preparedStatement, connection);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, preparedStatement, connection);
        }
        return false;
    }

    static void selectByPage(int pageNumber, int pageCount) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sqlx = "select * from jdbc_learn.student limit ?,?";
            preparedStatement = connection.prepareStatement(sqlx);
            /* 此处解决sql注入问题 */
/*            preparedStatement.setInt(1,startLine);
            preparedStatement.setInt(2,columnNumbers);*/
            preparedStatement.setInt(1, (pageNumber - 1) * pageCount); //自定义页面大小
            preparedStatement.setInt(2, pageCount);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                System.out.println(resultSet.getString("Id") + ","
                        + resultSet.getString("Name") + ","
                        + resultSet.getString("username") + ","
                        + resultSet.getString("stuNumber") + ","
                        + resultSet.getString("sex") + ","
                        + resultSet.getString("age") + ","
                );
        } catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, preparedStatement, connection);
        }

    }

    static void generateUserData(int numberOfColumn) throws ClassNotFoundException, SQLException {
        Connection connection = JDBCUtils.getConnection();
        Statement statement = connection.createStatement();
        //statement.execute("delete  from student where stuNumber='20191111453'");
        for (int i = 0; i < numberOfColumn; i++) {
            String name = "";
            for (int j = 0; j < 10; j++) {
                name += "abc".charAt((int) (Math.random() > 0.5 ? 0 : 1)) + "";
            }
            String sex = "man\twoman".split("\\t")[Math.random() > 0.5 ? 0 : 1];
            int age = (int) Math.round(Math.random() * 70);

            statement.execute(String.format("insert into jdbc_learn.student (Name,sex,age,stuNumber ,username,password)" +
                    "values ('%s','%s',%d,'%s','%s','%s')", name, sex, age, "20191111453", "username_" + name, (name + "@password").substring(0, 10)));
        }
        JDBCUtils.close(statement, connection);
    }
}
