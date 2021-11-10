package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    final static String PUBLIC_DNS = "long-term-well-solutions.cfyjyicdes3d.us-east-2.rds.amazonaws.com";
    final static String PORT = "3306";
    final static String DATABASE = "long-term-well-solutions";
    final static String REMOTE_DATABASE_USERNAME = "admin";
    final static String DATABASE_USER_PASSWORD = "javaivytech";


    public static Connection getConnection() {
        Connection connection = null;

        try {
            // Load the database driver for MySQL.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Set database connection parameters.
            String url = ("jdbc:mysql://"+PUBLIC_DNS+":"+PORT);
            String username = (REMOTE_DATABASE_USERNAME);
            String password = (DATABASE_USER_PASSWORD);

            // Create the database connection.
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Success");
            System.out.println(connection);
        }
        catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return connection;
    }
}
