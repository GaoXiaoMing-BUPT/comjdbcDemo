/**
 * Created with IntelliJ IDEA.
 * User: gxm
 * Date: 2019/10/15
 * Time: 23:12
 * To change this template use File | Settings | File Templates.
 * Description: 完成mysql的增删改查操作
 **/
package com.jdbc;

import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.log.LogFactory;

import javax.sql.DataSource;
import java.sql.*;

public class JDBCDemo02 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        boolean isSuccessSert = insert("gxm","qwe5201314");
        if (isSuccessSert)
            System.out.println("insert success");
        else
            System.out.println("insert fail");
        boolean isSuccessInsert = delete(16000);
        if (isSuccessInsert)
            System.out.println("delete success");
        else
            System.out.println("delete fail Id not exist");
        boolean isSuccessUpdate = update(16897,"123456789");
        if (isSuccessUpdate)
            System.out.println("update password success");
        else
            System.out.println("update password fail");
        transferAccounts("gxm", "qwe5201314", 100);

    }

    private static boolean insert(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        connection = JDBCUtils.getConnection();

        try {
            String sql = "insert into jdbc_learn.student (username, password) values (?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int i = preparedStatement.executeUpdate();  //返回受影响的行数  此处增加1行，故返回1
            return i == 1 ? true : false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBCUtils.close(preparedStatement, connection);
        return false;
    }

    private static boolean delete(int Id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        connection = JDBCUtils.getConnection();

        try {
            String sql = "delete from student where Id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Id);
            int i = preparedStatement.executeUpdate();  //返回受影响的行数  成功时大于0
            return i > 0 ? true : false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBCUtils.close(preparedStatement, connection);
        return false;
    }

    private static boolean update(int Id, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        connection = JDBCUtils.getConnection();

        try {
            String sql = "update student set password=? where Id=?";
            preparedStatement = connection.prepareStatement(sql);//根据问号顺序 设置 parameterIndex
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, Id);

            int i = preparedStatement.executeUpdate();  //返回受影响的行数  成功时大于0
            return i > 0 ? true : false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBCUtils.close(preparedStatement, connection);
        return false;
    }

    //事物管理器 保证事务同时成功同时失败，若不符合条件则回滚
    /*
     *  余额 a b
     *  a + 1000
     *  断电 数据库崩溃
     *  b - 1000
     * */
    private static void transferAccounts(String username1, String username2, int money) {
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        connection = JDBCUtils.getConnection();

        try {
            String sql1 = "update student set balance = balance - ? where username = ?";
            preparedStatement1 = connection.prepareStatement(sql1);//根据问号顺序 设置 parameterIndex
            connection.setAutoCommit(false);//关闭自动提交事务，改成手动提交 此时转账失败会自动回滚
            preparedStatement1.setInt(1, money);
            preparedStatement1.setString(2, username1);
            int flag1 = preparedStatement1.executeUpdate();  //返回受影响的行数  成功时大于0
            if (flag1 > 0)
                System.out.println("转出成功");
            else {
                System.out.println("转出失败");
                System.exit(1);
            }
            //"".charAt(3);//异常中断程序执行 事务均不会提交
            String sql2 = "update student set balance = balance + ? where username = ?";
            preparedStatement2 = connection.prepareStatement(sql2);//根据问号顺序 设置 parameterIndex
            preparedStatement2.setInt(1, money);
            preparedStatement2.setString(2, username2);
            int flag2 = preparedStatement2.executeUpdate();
            if (flag2 > 0)
                System.out.println("转入成功");
            else {
                System.out.println("转入失败");
                System.exit(1);
            }
            connection.commit();//提交事务
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBCUtils.close(preparedStatement1, preparedStatement2, connection);
    }
}
