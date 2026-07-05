package com.example.swapmart.usercontrol;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class mysqlconnection
{
    public static Connection getConnection()
    {
        Connection con=null;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://localhost/swapmarket","root","pass123");
            System.out.println("Network established");
        }
        catch(Exception exp)
        {
            System.out.println(exp.toString());
        }
        return con;
    }
}