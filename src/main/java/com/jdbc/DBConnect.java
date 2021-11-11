package com.jdbc;

import com.ltws.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class DBConnect {

    private final static String PUBLIC_DNS = "long-term-well-solutions.cfyjyicdes3d.us-east-2.rds.amazonaws.com";
    private final static String PORT = "3306";
    private final static String DATABASE = "ltws";
    private final static String REMOTE_DATABASE_USERNAME = "admin";
    private final static String DATABASE_USER_PASSWORD = "javaivytech";
    private static Connection conn = null;
    private static Statement st = null;
    private static ResultSet rs = null;

    public static Connection getConnection() {
        try {
            // Load the database driver for MySQL.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Set database connection parameters.
            String url = ("jdbc:mysql://" + PUBLIC_DNS + ":" + PORT + "/" + DATABASE);

            // Create the database connection.
            conn = DriverManager.getConnection(url, (REMOTE_DATABASE_USERNAME), (DATABASE_USER_PASSWORD));
            System.out.println("Success");
            System.out.println(conn);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    public static ObservableList<Employee> queryEmployees() {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        try {
            // get connection
            conn = DBConnect.getConnection();

            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT * FROM EMPLOYESS";

            // create the java statement
            st = conn.createStatement();

            // execute the query, and get a java resultset
            rs = st.executeQuery(query);

            // iterate through the java resultset

            while (rs.next()) {
                int id = rs.getInt("EMPLOYEE_ID");
                String firstName = rs.getString("EMPLOYEE_FNAME");
                String lastName = rs.getString("EMPLOYEE_LNAME");
                String email = rs.getString("EMPLOYEE_EMAIL");
                String address = rs.getString("EMPLOYEE_ADDRESS");
                //Date dateCreated = rs.getDate("date_created");
                //boolean isAdmin = rs.getBoolean("is_admin");
                //int numPoints = rs.getInt("num_points");
                employeeList.add(new Employee(id,firstName,lastName,email));
                // print the results
                System.out.println("this ran");
                System.out.format("%s, %s, %s\n", id, firstName, lastName);
            }
            return employeeList;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            return null;
        }
        finally {
            try { rs.close(); } catch (Exception e) { /* Ignored */ }
            try { st.close(); } catch (Exception e) { /* Ignored */ }
            try { conn.close(); } catch (Exception e) { /* Ignored */ }
        }
    }
}
