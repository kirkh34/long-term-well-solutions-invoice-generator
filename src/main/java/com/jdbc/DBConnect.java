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

    public static boolean validateEmployee(String username, String password) {
        boolean isValid = false;
        try {
            // get connection
            conn = DBConnect.getConnection();
            // our SQL SELECT query.
            String query = "SELECT * FROM employees WHERE user_name='" + username + "' and user_pass='" + password + "'";
            // create the java statement
            st = conn.createStatement();
            // execute the query, and get a java resultset
            rs = st.executeQuery(query);
            while(rs.next()) isValid = true;
            System.out.println("User is "+ isValid);
            return isValid;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            return false;
        } finally {
            try {
                rs.close();
            } catch (Exception e) { /* Ignored */ }
            try {
                st.close();
            } catch (Exception e) { /* Ignored */ }
            try {
                conn.close();
            } catch (Exception e) { /* Ignored */ }
        }
    }
    public static void updateEmployee(Employee emp){
        try{
            conn = DBConnect.getConnection();
            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            int admin =  emp.getIsAdmin() ? 1 : 0;
            String query = "UPDATE employees SET first_name='"+emp.getFirstName()+"', last_name='"+emp.getLastName()+"', user_name='"+emp.getUsername()+"', user_pass='"+emp.getPassword()+"', email='"+emp.getEmail()+"', street='"+emp.getStreet()+"', city='"+emp.getCity()+"', zip='"+emp.getZip()+"', phone='"+emp.getPhone()+"', ssn='"+emp.getSsn()+"', drivers_license='"+emp.getDl()+"', isAdmin='"+admin+"' WHERE id='"+emp.getID()+"'";
            // create the java statement
            st = conn.createStatement();

            // execute the query, and get a java resultset
            st.executeUpdate(query);
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
            finally {
            try { rs.close(); } catch (Exception e) { /* Ignored */ }
            try { st.close(); } catch (Exception e) { /* Ignored */ }
            try { conn.close(); } catch (Exception e) { /* Ignored */ }
        }
    }

    public static ObservableList<Employee> queryEmployees() {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        try {
            // get connection
            conn = DBConnect.getConnection();

            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT * FROM employees";

            // create the java statement
            st = conn.createStatement();

            // execute the query, and get a java resultset
            rs = st.executeQuery(query);

            // iterate through the java resultset

            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String username = rs.getString("user_name");
                String street = rs.getString("street");
                String city = rs.getString("city");
                int zip = rs.getInt("zip");
                int phone = rs.getInt("phone");
                int ssn = rs.getInt("ssn");
                int dl = rs.getInt("drivers_license");
                boolean isAdmin = rs.getBoolean("isAdmin");
                //Date dateCreated = rs.getDate("date_created");
                //int numPoints = rs.getInt("num_points");
                employeeList.add(new Employee(id,firstName,lastName,email, street, city, zip, phone, ssn, dl, username, isAdmin));
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
