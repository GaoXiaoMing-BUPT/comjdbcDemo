/**
 * Created with IntelliJ IDEA.
 * User: gxm
 * Date: 2019/10/20
 * Time: 9:47
 * To change this template use File | Settings | File Templates.
 * Description:
 **/
package com.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBCPDemo {
    public static void main(String[] args) {
        transferAccountsDPCP("gxm", "qwe5201314", 100);
    }
    private static void transferAccountsDPCP(String username1, String username2, int money) {
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        connection = DBCPDataSource.getConnection();
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
            //"".charAt(3);//异常中断程序执行 测试commit事务是否被提交
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
            //connection.commit();//提交事务
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCPDataSource.close(connection,preparedStatement1,preparedStatement2);
        }

    }
}
