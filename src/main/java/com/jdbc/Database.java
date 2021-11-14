package com.jdbc;

import com.ltws.Employee;
import com.ui.ViewEmployeePane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class Database {

    private final static String PUBLIC_DNS = "long-term-well-solutions.cfyjyicdes3d.us-east-2.rds.amazonaws.com";
    private final static String PORT = "3306";
    private final static String DATABASE = "ltws";
    private final static String DB_USERNAME = "admin";
    private final static String DB_PASSWORD = "javaivytech";
    private static Connection conn = null;
    private static Statement st = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;

    public static Connection getConnection() {
        try {
            // Load the database driver for MySQL.
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Set database connection parameters.
            String url = ("jdbc:mysql://" + PUBLIC_DNS + ":" + PORT + "/" + DATABASE);
            // Create the database connection.
            conn = DriverManager.getConnection(url, (DB_USERNAME), (DB_PASSWORD));
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    //////////////////
    //Employee queries
    //////////////////

    // Validate employee login and return employee object for employee logged in
    public static Employee validateEmployee(String username, String password) {
        boolean isValid = false;
        Employee empLoggedIn = null;
        try {
            // Get Connection
            conn = getConnection();
            // Our SQL SELECT query.
            String query = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_USERNAME=? and EMPLOYEE_PASSWORD=?";
            // Create prepared statement with query
            ps = conn.prepareCall(query);
            // Set values in query
            ps.setString(1, username);
            ps.setString(2, password);
            // Execute the query, and get a ResultSet
            rs = ps.executeQuery();
            // Iterate through ResultSet
            while(rs.next())
            {
                // Set employee who is logged in to static object
                empLoggedIn = newEmployeeFromDb(rs);
                // If ResultSet returned a record set flag true
                isValid = true;
            }
            // Return employee logged in object
            if(isValid) {
                System.out.println("DB - Validate Employee");
                return empLoggedIn;
            }
            else
                return null;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            return null;
        }finally {
                //Clean up
                try { rs.close(); } catch (Exception e) { /* Ignored */ }
                try { ps.close(); } catch (Exception e) { /* Ignored */ }
                try { conn.close(); } catch (Exception e) { /* Ignored */ }
            }
    }

    //Update single employee record in the DB by id
    public static void updateEmployee(Employee emp){
        try{
            conn = Database.getConnection();
            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            int admin =  emp.getIsAdmin() ? 1 : 0;
            String query = "UPDATE EMPLOYEE SET EMPLOYEE_FNAME=?, EMPLOYEE_LNAME=?, EMPLOYEE_USERNAME=?, EMPLOYEE_PASSWORD=?, EMPLOYEE_EMAIL=?, EMPLOYEE_STREET=?, EMPLOYEE_CITY=?, EMPLOYEE_STATE=?, EMPLOYEE_ZIP=?, EMPLOYEE_PHONE=?, EMPLOYEE_SSN=?, EMPLOYEE_DLN=?, EMPLOYEE_ADMIN=? WHERE EMPLOYEE_ID=?";
            // Create prepared statement with query
            ps = conn.prepareCall(query);
            // Set values in query
            setEmployeeValues(emp, ps, admin);
            ps.setInt(14, emp.getID());
            // Execute UPDATE query
            ps.executeUpdate();
            System.out.println("DB - Update Employee");
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

    //Add single employee to the DB
    public static void insertEmployee(Employee emp){
        try{
            conn = Database.getConnection();
            int admin =  emp.getIsAdmin() ? 1 : 0;
            String query = "INSERT INTO EMPLOYEE (EMPLOYEE_FNAME, EMPLOYEE_LNAME, EMPLOYEE_USERNAME, EMPLOYEE_PASSWORD, EMPLOYEE_EMAIL, EMPLOYEE_STREET, EMPLOYEE_CITY, EMPLOYEE_STATE, EMPLOYEE_ZIP, EMPLOYEE_PHONE, EMPLOYEE_SSN, EMPLOYEE_DLN, EMPLOYEE_ADMIN) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            //ps = conn.prepareCall(query);
            //Set values
            setEmployeeValues(emp, ps, admin);
            // execute the query
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                ViewEmployeePane.lastInsertID = rs.getInt(1);
            }
            System.out.println("DB - Insert Employee");
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            try { ps.close(); } catch (Exception e) { /* Ignored */ }
            try { conn.close(); } catch (Exception e) { /* Ignored */ }
        }
    }

    //Get all employees and put them in an Observable List for TableView use
    public static ObservableList<Employee> queryEmployees() {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        try {
            // get connection
            conn = getConnection();
            // our SQL SELECT query.
            String query = "SELECT * FROM EMPLOYEE";
            ps = conn.prepareCall(query);
            rs = ps.executeQuery();
            // iterate through the java ResultSet
            while (rs.next()) {
                employeeList.add(newEmployeeFromDb(rs));
            }
            System.out.println("DB - Query Employees");
            return employeeList;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            return null;
        }
        finally {
            try { rs.close(); } catch (Exception e) { /* Ignored */ }
            try { ps.close(); } catch (Exception e) { /* Ignored */ }
            try { conn.close(); } catch (Exception e) { /* Ignored */ }
        }
    }

    //Delete single employee from DB by id
    public static void deleteEmployee(int id) {
        try{
            conn = Database.getConnection();
            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID=?";
            // create the java statement
            ps = conn.prepareCall(query);
            ps.setInt(1, id);
            // execute query
            ps.executeUpdate();
            System.out.println("DB - Delete Employee");
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            try { ps.close(); } catch (Exception e) { /* Ignored */ }
            try { conn.close(); } catch (Exception e) { /* Ignored */ }
        }
    }

    // Set values of prepared statement of whole table (except for id)
    private static void setEmployeeValues(Employee emp, PreparedStatement ps, int admin) throws SQLException {
        ps.setString(1, emp.getFirstName().trim());
        ps.setString(2, emp.getLastName().trim());
        ps.setString(3, emp.getUsername().trim());
        ps.setString(4, emp.getPassword().trim());
        ps.setString(5, emp.getEmail().trim());
        ps.setString(6, emp.getStreet().trim());
        ps.setString(7, emp.getCity().trim());
        ps.setString(8, emp.getState().trim());
        ps.setInt(9, emp.getZip());
        ps.setLong(10, emp.getPhone());
        ps.setInt(11, emp.getSsn());
        ps.setLong(12, emp.getDln());
        ps.setInt(13, admin);
    }

    // Create a populated employee object from SELECT employee statement
    private static Employee newEmployeeFromDb(ResultSet rs) throws SQLException {
        int id = rs.getInt("EMPLOYEE_ID");
        String firstName = rs.getString("EMPLOYEE_FNAME");
        String lastName = rs.getString("EMPLOYEE_LNAME");
        String email = rs.getString("EMPLOYEE_EMAIL");
        String username = rs.getString("EMPLOYEE_USERNAME");
        String password = rs.getString("EMPLOYEE_PASSWORD");
        String street = rs.getString("EMPLOYEE_STREET");
        String city = rs.getString("EMPLOYEE_CITY");
        String state = rs.getString("EMPLOYEE_STATE");
        int zip = rs.getInt("EMPLOYEE_ZIP");
        int phone = rs.getInt("EMPLOYEE_PHONE");
        int ssn = rs.getInt("EMPLOYEE_SSN");
        int dl = rs.getInt("EMPLOYEE_DLN");
        boolean isAdmin = rs.getBoolean("EMPLOYEE_ADMIN");

        return new Employee(id,firstName,lastName,email, street, city, state, zip, phone, ssn, dl, username, password, isAdmin);
    }
}
