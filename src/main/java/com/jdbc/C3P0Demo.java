/**
 * Created with IntelliJ IDEA.
 * User: gxm
 * Date: 2019/10/20
 * Time: 9:50
 * To change this template use File | Settings | File Templates.
 * Description:
 **/
package com.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class C3P0Demo {
    public static void main(String[] args) {
        transferAccountsC3P0("username_aaaaaaaaaa","gxm",100);
    }
    private static void transferAccountsC3P0(String username1,String username2,int money){
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        Connection connection = C3P0DataSource.getConnection();
        try {
            connection.setAutoCommit(false);//关闭自动提交  防止运行时出错，导致数据库数据被错误修改
            String sql = "update student set balance = balance - ? where username = ?";
            preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setInt(1,money);
            preparedStatement1.setString(2,username1);
            int update1 = preparedStatement1.executeUpdate();
            if (update1 > 0)
                System.out.println("转出成功");
            else
                System.out.println("转出失败");
            sql = "update student set balance = balance + ? where username = ?";
            preparedStatement2 = connection.prepareStatement(sql);
            preparedStatement2.setInt(1,money);
            preparedStatement2.setString(2,username2);
            int update2 = preparedStatement2.executeUpdate();
            if (update2 > 0)
                System.out.println("转入成功");
            else
                System.out.println("转出成功");
            connection.commit();//提交事务

        } catch (SQLException e) {
            C3P0DataSource.close(connection,preparedStatement1,preparedStatement2);
            e.printStackTrace();
        }
    }
}
