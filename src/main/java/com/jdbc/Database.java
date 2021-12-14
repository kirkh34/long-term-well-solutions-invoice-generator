package com.jdbc;

import com.ltws.Customer;
import com.ltws.Employee;
import com.ltws.Job;
import com.ui.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;


public class Database {

    private final static String PUBLIC_DNS = "gator3139.hostgator.com";
    private final static String PORT = "3306";
    private final static String DATABASE = "avidnexu_long_term_well_solutions";
    private final static String DB_USERNAME = "avidnexu_ltws";
    private final static String DB_PASSWORD = "sdev265ivytech";
    private static Connection conn = null;
    private static Statement st = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;

    public static Connection getConnection() {
        try {
            // Load the database driver for MySQL.
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Set database connection parameters.
            String url = ("jdbc:mysql://" + PUBLIC_DNS + ":" + PORT + "/" + DATABASE + "?useSSL=false");
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
            String query = "SELECT EMPLOYEE.EMPLOYEE_ID, EMPLOYEE.EMPLOYEE_FNAME, EMPLOYEE.EMPLOYEE_LNAME, EMPLOYEE.EMPLOYEE_EMAIL, EMPLOYEE.EMPLOYEE_PHONE, EMPLOYEE_INFO.EMPLOYEE_SSN, EMPLOYEE_INFO.EMPLOYEE_USERNAME, EMPLOYEE_INFO.EMPLOYEE_PASSWORD, EMPLOYEE_INFO.EMPLOYEE_DLN, EMPLOYEE_INFO.EMPLOYEE_STREET, EMPLOYEE_INFO.EMPLOYEE_CITY, EMPLOYEE_INFO.EMPLOYEE_STATE, EMPLOYEE_INFO.EMPLOYEE_ZIP, EMPLOYEE_INFO.EMPLOYEE_ADMIN FROM EMPLOYEE INNER JOIN EMPLOYEE_INFO ON EMPLOYEE.EMPLOYEE_ID=EMPLOYEE_INFO.EMPLOYEE_ID WHERE EMPLOYEE_INFO.EMPLOYEE_USERNAME=? AND EMPLOYEE_INFO.EMPLOYEE_PASSWORD=?";
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
            String query = "UPDATE EMPLOYEE SET EMPLOYEE_FNAME=?, EMPLOYEE_LNAME=?, EMPLOYEE_EMAIL=?, EMPLOYEE_PHONE=? WHERE EMPLOYEE_ID=?";
            // Create prepared statement with query
            ps = conn.prepareCall(query);
            // Set values in query
            setEmployeeValues(emp, ps);
            ps.setInt(5, emp.getID());
            // Execute UPDATE query
            ps.executeUpdate();
            System.out.println("DB - Update Employee");
            ps.close();
            String query2 = "UPDATE EMPLOYEE_INFO SET EMPLOYEE_SSN=?, EMPLOYEE_USERNAME=?, EMPLOYEE_PASSWORD=?, EMPLOYEE_DLN=?, EMPLOYEE_STREET=?, EMPLOYEE_CITY=?, EMPLOYEE_STATE=?, EMPLOYEE_ZIP=?, EMPLOYEE_ADMIN=? WHERE EMPLOYEE_ID=?";
            ps = conn.prepareCall(query2);
            setEmployeeInfoValues(emp, ps, admin, emp.getID());
            ps.executeUpdate();
            System.out.println("DB - Update Employee Info");

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
            int newID = 0;
            String query = "INSERT INTO EMPLOYEE (EMPLOYEE_FNAME, EMPLOYEE_LNAME, EMPLOYEE_EMAIL, EMPLOYEE_PHONE) VALUES (?,?,?,?)";
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            //ps = conn.prepareCall(query);
            //Set values
            setEmployeeValues(emp, ps);
            // execute the query
            ps.executeUpdate();
            System.out.println("DB - Insert Employee");
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                ViewEmployeePane.lastInsertID = rs.getInt(1);
                newID = rs.getInt(1);
            }
            ps.close();
            String query2 = "INSERT INTO EMPLOYEE_INFO (EMPLOYEE_SSN, EMPLOYEE_USERNAME, EMPLOYEE_PASSWORD, EMPLOYEE_DLN, EMPLOYEE_STREET, EMPLOYEE_CITY, EMPLOYEE_STATE, EMPLOYEE_ZIP, EMPLOYEE_ADMIN, EMPLOYEE_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareCall(query2);
            setEmployeeInfoValues(emp, ps, admin, newID);
            ps.executeUpdate();

            System.out.println("DB - Insert Employee Info");
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
            String query = "SELECT EMPLOYEE.EMPLOYEE_ID, EMPLOYEE.EMPLOYEE_FNAME, EMPLOYEE.EMPLOYEE_LNAME, EMPLOYEE.EMPLOYEE_EMAIL, EMPLOYEE.EMPLOYEE_PHONE, EMPLOYEE_INFO.EMPLOYEE_SSN, EMPLOYEE_INFO.EMPLOYEE_USERNAME, EMPLOYEE_INFO.EMPLOYEE_PASSWORD, EMPLOYEE_INFO.EMPLOYEE_DLN, EMPLOYEE_INFO.EMPLOYEE_STREET, EMPLOYEE_INFO.EMPLOYEE_CITY, EMPLOYEE_INFO.EMPLOYEE_STATE, EMPLOYEE_INFO.EMPLOYEE_ZIP, EMPLOYEE_INFO.EMPLOYEE_ADMIN FROM EMPLOYEE INNER JOIN EMPLOYEE_INFO ON EMPLOYEE.EMPLOYEE_ID=EMPLOYEE_INFO.EMPLOYEE_ID ORDER BY EMPLOYEE.EMPLOYEE_ID ASC";
            ps = conn.prepareCall(query);
            rs = ps.executeQuery();
            // iterate through the java ResultSet
            while (rs.next()) {
                employeeList.add(newEmployeeFromDb(rs));
            }
            System.out.println("DB - Query Employees");
            return employeeList;
        } catch (Exception e) {
            System.err.println("Got an exception!");
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
    private static void setEmployeeValues(Employee emp, PreparedStatement ps) throws SQLException {
        ps.setString(1, emp.getFirstName().trim());
        ps.setString(2, emp.getLastName().trim());
        ps.setString(3, emp.getEmail().trim());
        ps.setLong(4, emp.getPhone());
    }

    // Set values of prepared statement of whole table (except for id)
    private static void setEmployeeInfoValues(Employee emp, PreparedStatement ps, int admin, int id) throws SQLException {
        ps.setInt(1, emp.getSsn());
        ps.setString(2, emp.getUsername().trim());
        ps.setString(3, emp.getPassword().trim());
        ps.setLong(4, emp.getDln());
        ps.setString(5, emp.getStreet().trim());
        ps.setString(6, emp.getCity().trim());
        ps.setString(7, emp.getState().trim());
        ps.setInt(8, emp.getZip());
        ps.setInt(9, admin);
        ps.setInt(10, id);
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
        long phone = rs.getLong("EMPLOYEE_PHONE");
        int ssn = rs.getInt("EMPLOYEE_SSN");
        long dl = rs.getLong("EMPLOYEE_DLN");
        boolean isAdmin = rs.getBoolean("EMPLOYEE_ADMIN");

        return new Employee(id,firstName,lastName,email, street, city, state, zip, phone, ssn, dl, username, password, isAdmin);
    }

    //////////////////
    //Customer queries
    //////////////////

    //Get all customers and put them in an Observable List for TableView use
    public static ObservableList<Customer> queryCustomers() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            // get connection
            conn = getConnection();
            // our SQL SELECT query.
            String query = "SELECT CUSTOMER.CUSTOMER_ID, CUSTOMER.CUSTOMER_FNAME, CUSTOMER.CUSTOMER_LNAME, CUSTOMER.CUSTOMER_EMAIL, CUSTOMER.CUSTOMER_PHONE, CUSTOMER.CUSTOMER_STREET, CUSTOMER.CUSTOMER_CITY, CUSTOMER.CUSTOMER_STATE, CUSTOMER.CUSTOMER_ZIP, CUSTOMER.CUSTOMER_RATING, CUSTOMER.WELL_DEPTH, CUSTOMER.WELL_LOCATION, CUSTOMER.WELL_FLOWRATE, CC_INFO.CC_NUMBER, CC_INFO.CC_EXPIRATION, CC_INFO.CC_CVC, CC_INFO.CC_ZIP FROM CUSTOMER LEFT JOIN CC_INFO ON CUSTOMER.CUSTOMER_ID=CC_INFO.CUSTOMER_ID ORDER BY CUSTOMER.CUSTOMER_ID ASC";
            ps = conn.prepareCall(query);
            rs = ps.executeQuery();
            // iterate through the java ResultSet
            while (rs.next()) {
                customerList.add(newCustomerFromDb(rs));
            }
            System.out.println("DB - Query Customers");
            return customerList;
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
            return null;
        }
        finally {
            try { rs.close(); } catch (Exception e) { /* Ignored */ }
            try { ps.close(); } catch (Exception e) { /* Ignored */ }
            try { conn.close(); } catch (Exception e) { /* Ignored */ }
        }
    }

    //Add single customer to the DB
    public static void insertCustomer(Customer cust){
        try{
            conn = Database.getConnection();
            int newID = 0;
            String query = "INSERT INTO CUSTOMER (CUSTOMER_FNAME, CUSTOMER_LNAME, CUSTOMER_EMAIL, CUSTOMER_PHONE, CUSTOMER_STREET, CUSTOMER_CITY, CUSTOMER_STATE, CUSTOMER_ZIP, CUSTOMER_RATING, WELL_DEPTH, WELL_FLOWRATE, WELL_LOCATION) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            //ps = conn.prepareCall(query);
            //Set values
            setCustomerValues(cust, ps);
            // execute the query
            ps.executeUpdate();
            System.out.println("DB - Insert Customer");
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                ViewCustomersPane.lastInsertID_Customer = rs.getInt(1);
                newID = rs.getInt(1);
            }
            ps.close();
            String query2 = "INSERT INTO CC_INFO (CC_NUMBER, CC_EXPIRATION, CC_CVC, CC_ZIP, CUSTOMER_ID) VALUES (?,?,?,?,?)";
            ps = conn.prepareCall(query2);
            setCustomerCCValues(cust, ps, newID);
            ps.executeUpdate();

            System.out.println("DB - Insert Customer CC Info");
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            try { ps.close(); } catch (Exception e) { /* Ignored */ }
            try { conn.close(); } catch (Exception e) { /* Ignored */ }
        }
    }

    //Update single customer record in the DB by id
    public static void updateCustomer(Customer cust){
        try{
            conn = Database.getConnection();
            String query = "UPDATE CUSTOMER SET CUSTOMER_FNAME=?, CUSTOMER_LNAME=?, CUSTOMER_EMAIL=?, CUSTOMER_PHONE=?, CUSTOMER_STREET=?, CUSTOMER_CITY=?, CUSTOMER_STATE=?, CUSTOMER_ZIP=?, CUSTOMER_RATING=?, WELL_DEPTH=?, WELL_FLOWRATE=?, WELL_LOCATION=? WHERE CUSTOMER_ID=?";
            // Create prepared statement with query
            ps = conn.prepareCall(query);
            // Set values in query
            setCustomerValues(cust, ps);
            ps.setInt(13, cust.getID());
            // Execute UPDATE query
            ps.executeUpdate();
            System.out.println("DB - Update CUSTOMER");
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

    //Update single customer cc record in the DB by id
    public static void updatePayment(Customer cust){
        try{
            conn = Database.getConnection();
            // Create prepared statement with query
            String query = "UPDATE CC_INFO SET CC_NUMBER=?, CC_EXPIRATION=?, CC_CVC=?, CC_ZIP=? WHERE CUSTOMER_ID=?";
            ps = conn.prepareCall(query);
            // Set values in query
            setCustomerCCValues(cust, ps, cust.getID());
            ps.executeUpdate();
            System.out.println("DB - Update Customer CC Info");

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

    //Delete single customer from DB by id
    public static void deleteCustomer(int id) {
        try{
            conn = Database.getConnection();
            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID=?";
            // create the java statement
            ps = conn.prepareCall(query);
            ps.setInt(1, id);
            // execute query
            ps.executeUpdate();
            System.out.println("DB - Delete Customer");
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            try { ps.close(); } catch (Exception e) { /* Ignored */ }
            try { conn.close(); } catch (Exception e) { /* Ignored */ }
        }
    }

    //Delete single customer cc from DB by id
    public static void deletePayment(int id) {
        try{
            conn = Database.getConnection();
            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "DELETE FROM CC_INFO WHERE CUSTOMER_ID=?";
            // create the java statement
            ps = conn.prepareCall(query);
            ps.setInt(1, id);
            // execute query
            ps.executeUpdate();
            System.out.println("DB - Delete Customer CC");
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            try { ps.close(); } catch (Exception e) { /* Ignored */ }
            try { conn.close(); } catch (Exception e) { /* Ignored */ }
        }
    }

    // Create a populated employee object from SELECT employee statement
    private static Customer newCustomerFromDb(ResultSet rs) throws SQLException {
        int id = rs.getInt("CUSTOMER_ID");
        String firstName = rs.getString("CUSTOMER_FNAME");
        String lastName = rs.getString("CUSTOMER_LNAME");
        String street = rs.getString("CUSTOMER_STREET");
        String city = rs.getString("CUSTOMER_CITY");
        String state = rs.getString("CUSTOMER_STATE");
        int zip = rs.getInt("CUSTOMER_ZIP");
        long phone = rs.getLong("CUSTOMER_PHONE");
        String email = rs.getString("CUSTOMER_EMAIL");
        int rating = rs.getInt("CUSTOMER_RATING");
        int wellDepth = rs.getInt("WELL_DEPTH");
        String wellLocation = rs.getString("WELL_LOCATION");
        String wellFlowRate = rs.getString("WELL_FLOWRATE");
        long ccNumber = rs.getLong("CC_NUMBER");
        int ccExp = rs.getInt("CC_EXPIRATION");
        int ccCVC = rs.getInt("CC_CVC");
        int ccZip = rs.getInt("CC_ZIP");


        return new Customer(id, firstName, lastName, street, city, state, zip, phone, email, rating, wellDepth, wellLocation, wellFlowRate, ccNumber, ccExp, ccCVC, ccZip);
    }

    // Set values of prepared statement of whole table (except for PK)
    private static void setCustomerValues(Customer cust, PreparedStatement ps) throws SQLException {
        ps.setString(1, cust.getFirstName().trim());
        ps.setString(2, cust.getLastName().trim());
        ps.setString(3, cust.getEmail().trim());
        ps.setLong(4, cust.getPhone());
        ps.setString(5, cust.getStreet().trim());
        ps.setString(6, cust.getCity().trim());
        ps.setString(7, cust.getState().trim());
        ps.setInt(8, cust.getZip());
        ps.setInt(9, cust.getRating());
        ps.setInt(10, cust.getWellDepth());
        ps.setString(11, cust.getWellFlowRate().trim());
        ps.setString(12, cust.getWellLocation().trim());
    }

    private static void setCustomerCCValues(Customer cust, PreparedStatement ps, int id) throws SQLException {
        ps.setLong(1, cust.getCcNumber());
        ps.setInt(2, cust.getCcExp());
        ps.setInt(3, cust.getCcCvc());
        ps.setInt(4, cust.getCcZip());
        ps.setInt(5, id);
    }

    //////////////////
    //Job queries
    //////////////////

    //Get all jobs and put them in an Observable List for TableView use
    public static ObservableList<Job> queryJobs() {
        ObservableList<Job> jobList = FXCollections.observableArrayList();
        ArrayList<Integer> jobEmployeeIdList = new ArrayList();
        ObservableList<Job.Material> materialList = FXCollections.observableArrayList();
        ObservableList<Job.Labor> laborList = FXCollections.observableArrayList();
        ObservableList<Job.Fee> feeList = FXCollections.observableArrayList();
        int prevId = 0;
        int currentId = 0;
        int currentCustId = 0;
        Date currentJobStart = null;
        Date currentJobEnd = null;
        String currentJobDesc = null;
        boolean currentJobIsPaid = false;
        try {
            // get connection
            conn = getConnection();
            // our SQL SELECT query.
            String query = "SELECT JOB.*, JOB_EMPLOYEE.JOB_EMPLOYEE_ID, MATERIAL.MATERIAL_DESCRIPTION, MATERIAL.MATERIAL_PRICE, LABOR.LABOR_DESCRIPTION, LABOR.LABOR_PRICE, FEE.FEE_DESCRIPTION, FEE.FEE_PRICE FROM JOB LEFT JOIN JOB_EMPLOYEE ON JOB.JOB_ID=JOB_EMPLOYEE.JOB_ID LEFT JOIN MATERIAL ON JOB.JOB_ID = MATERIAL.JOB_ID LEFT JOIN LABOR ON JOB.JOB_ID = LABOR.JOB_ID LEFT JOIN FEE ON JOB.JOB_ID = FEE.JOB_ID";
            ps = conn.prepareCall(query);
            rs = ps.executeQuery();

            // iterate through the java ResultSet
            while (rs.next()) {
                if(rs.isFirst()) prevId = rs.getInt("JOB_ID");
                if(rs.getInt("JOB_ID") != prevId){

                    ArrayList<Integer> currentEmpList = new ArrayList<>();
                    ObservableList<Job.Material> currentMaterialList = FXCollections.observableArrayList();
                    ObservableList<Job.Labor> currentLaborList = FXCollections.observableArrayList();
                    ObservableList<Job.Fee> currentFeeList = FXCollections.observableArrayList();
                    for(int id : jobEmployeeIdList){
                        currentEmpList.add(id);
                    }
                    for(Job.Material mat: materialList){
                        currentMaterialList.add(mat);
                    }
                    for(Job.Labor labor: laborList){
                        currentLaborList.add(labor);
                    }
                    for(Job.Fee fee: feeList){
                        currentFeeList.add(fee);
                    }

                    Job currentJob = new Job(currentId, currentCustId, currentEmpList, currentJobStart.toLocalDate(), currentJobEnd.toLocalDate(), currentJobDesc, currentJobIsPaid, currentMaterialList, currentLaborList, currentFeeList);
                    jobList.add(currentJob);
                    jobEmployeeIdList.clear();
                    materialList.clear();
                    laborList.clear();
                    feeList.clear();
                    prevId = rs.getInt("JOB_ID");
                }

                currentId = rs.getInt("JOB_ID");
                currentCustId = rs.getInt("JOB_CUSTOMER");
                currentJobStart = rs.getDate("JOB_START");
                currentJobEnd = rs.getDate("JOB_END");
                currentJobDesc = rs.getString("JOB_DESCRIPTION");
                currentJobIsPaid = rs.getBoolean("JOB_PAID");

                //Build Job Employee List
                int empID = rs.getInt("JOB_EMPLOYEE_ID");
                if (!jobEmployeeIdList.contains(empID) && empID != 0) {
                    jobEmployeeIdList.add(empID);
                }
                //Build Material List
                String materialDesc = rs.getString("MATERIAL_DESCRIPTION");
                double materialPrice = rs.getDouble("MATERIAL_PRICE");
                Job.Material newMaterial = new Job.Material(materialDesc, materialPrice);
                if (materialDesc != null && !materialList.contains(newMaterial)) {
                    materialList.add(newMaterial);
                }
                //Build Labor List
                String laborDesc = rs.getString("LABOR_DESCRIPTION");
                double laborPrice = rs.getDouble("LABOR_PRICE");
                Job.Labor newLabor = new Job.Labor(laborDesc, laborPrice);
                if (laborDesc != null && !laborList.contains(newLabor)) {
                    laborList.add(newLabor);
                }
                //Build Fee List
                String feeDesc = rs.getString("FEE_DESCRIPTION");
                double feePrice = rs.getDouble("FEE_PRICE");
                Job.Fee newFee = new Job.Fee(feeDesc, feePrice);
                if (feeDesc != null && !feeList.contains(newFee)) {
                    feeList.add(newFee);
                }
                if(rs.isLast()){
                    Job currentJob = new Job(currentId, currentCustId, jobEmployeeIdList, currentJobStart.toLocalDate(), currentJobEnd.toLocalDate(), currentJobDesc, currentJobIsPaid, materialList, laborList, feeList);
                    jobList.add(currentJob);
                }
            }
            System.out.println("DB - Query Jobs");
            return jobList;
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
            System.err.println();
            e.printStackTrace();
            return null;
        }
        finally {
            try { rs.close(); } catch (Exception e) { /* Ignored */ }
            try { ps.close(); } catch (Exception e) { /* Ignored */ }
            try { conn.close(); } catch (Exception e) { /* Ignored */ }
        }
    }


    //Update single job record in the DB by id
    public static void updateJob(Job job){

        boolean jobChanged = false;
        for(Job j : LoginPageController.allJobs) {
            if (j.getID() == job.getID()) {
                if (j.getInvoicePaid() != job.getInvoicePaid()) jobChanged = true;
                if (!(j.getJobDesc().equals(job.getJobDesc()))) jobChanged = true;
                if (!(j.getJobStart().equals(job.getJobStart()))) jobChanged = true;
                if (!(j.getJobEnd().equals(job.getJobEnd()))) jobChanged = true;
            }
        }

        try{
            conn = Database.getConnection();

            if(jobChanged) {
                // our SQL SELECT query.
                int invoicePaid = job.getInvoicePaid() ? 1 : 0;
                String query = "UPDATE JOB SET JOB_START=?, JOB_END=?, JOB_DESCRIPTION=?, JOB_PAID=? WHERE JOB_ID=?";
                // Create prepared statement with query
                ps = conn.prepareCall(query);
                // Set values in query
                setJobValues(job, ps, invoicePaid);
                ps.setInt(5, job.getID());
                // Execute UPDATE query
                ps.executeUpdate();
                System.out.println("DB - Update Job");
                ps.close();

            }
            boolean empListMatching = false;
            for(Job j : LoginPageController.allJobs){
                if (j.getID() == job.getID()){
                    Collections.sort(j.getJobEmployees());
                    Collections.sort(job.getJobEmployees());
                    empListMatching = j.getJobEmployees().equals(job.getJobEmployees());
                }
            }

            if(!empListMatching) {
                String query2 = "DELETE FROM JOB_EMPLOYEE WHERE JOB_ID=?";
                // create the java statement
                ps = conn.prepareCall(query2);
                ps.setInt(1, job.getID());
                // execute query
                ps.executeUpdate();
                System.out.println("DB - Remove Employees on Job");

                String values = "";

                for (int i = 0; i < job.getJobEmployees().size(); i++) {
                    String comma = i == (job.getJobEmployees().size() - 1) ? "" : ",";
                    String row = "(" + job.getJobEmployees().get(i) + "," + job.getID() + ")" + comma;
                    values += row;
                }
                String query3 = "INSERT INTO JOB_EMPLOYEE (JOB_EMPLOYEE_ID, JOB_ID) VALUES " + values;

                st = conn.createStatement();
                st.executeUpdate(query3);
                System.out.println("DB - Insert Employees on Job");
            }

            boolean matListMatching = false;
            for(Job j : LoginPageController.allJobs){
                if (j.getID() == job.getID()){
                    matListMatching = j.getMaterialOList().equals(job.getMaterialOList());
                }
            }
            if(!matListMatching) {
                String query4 = "DELETE FROM MATERIAL WHERE JOB_ID=?";
                // create the java statement
                ps = conn.prepareCall(query4);
                ps.setInt(1, job.getID());
                // execute query
                ps.executeUpdate();
                System.out.println("DB - Remove Materials on Job");

                String values = "";

                for (int i = 0; i < job.getMaterialOList().size(); i++) {
                    String comma = i == (job.getMaterialOList().size() - 1) ? "" : ",";
                    String row = "(" + job.getMaterialOList().get(i).getItemPrice() + ",'" + job.getMaterialOList().get(i).getItemDesc() + "'," + job.getID() + ")" + comma;
                    values += row;
                }
                String query5 = "INSERT INTO MATERIAL (MATERIAL_PRICE, MATERIAL_DESCRIPTION, JOB_ID) VALUES " + values;

                st = conn.createStatement();
                st.executeUpdate(query5);
                System.out.println("DB - Insert Materials on Job");
            }

            boolean laborListMatching = false;
            for(Job j : LoginPageController.allJobs){
                if (j.getID() == job.getID()){
                    laborListMatching = j.getLaborOList().equals(job.getLaborOList());
                }
            }
            if(!laborListMatching) {
                String query6 = "DELETE FROM LABOR WHERE JOB_ID=?";
                // create the java statement
                ps = conn.prepareCall(query6);
                ps.setInt(1, job.getID());
                // execute query
                ps.executeUpdate();
                System.out.println("DB - Remove Labor on Job");

                String values = "";

                for (int i = 0; i < job.getLaborOList().size(); i++) {
                    String comma = i == (job.getLaborOList().size() - 1) ? "" : ",";
                    String row = "(" + job.getLaborOList().get(i).getItemPrice() + ",'" + job.getLaborOList().get(i).getItemDesc() + "'," + job.getID() + ")" + comma;
                    values += row;
                }
                String query7 = "INSERT INTO LABOR (LABOR_PRICE, LABOR_DESCRIPTION, JOB_ID) VALUES " + values;

                st = conn.createStatement();
                st.executeUpdate(query7);
                System.out.println("DB - Insert Labor on Job");
            }

            boolean feeListMatching = false;
            for(Job j : LoginPageController.allJobs){
                if (j.getID() == job.getID()){
                    feeListMatching = j.getFeeOList().equals(job.getFeeOList());
                }
            }
            if(!feeListMatching) {
                String query8 = "DELETE FROM FEE WHERE JOB_ID=?";
                // create the java statement
                ps = conn.prepareCall(query8);
                ps.setInt(1, job.getID());
                // execute query
                ps.executeUpdate();
                System.out.println("DB - Remove Fee on Job");

                String values = "";

                for (int i = 0; i < job.getFeeOList().size(); i++) {
                    String comma = i == (job.getFeeOList().size() - 1) ? "" : ",";
                    String row = "(" + job.getFeeOList().get(i).getItemPrice() + ",'" + job.getFeeOList().get(i).getItemDesc() + "'," + job.getID() + ")" + comma;
                    values += row;
                }
                String query9 = "INSERT INTO FEE (FEE_PRICE, FEE_DESCRIPTION, JOB_ID) VALUES " + values;

                st = conn.createStatement();
                st.executeUpdate(query9);
                System.out.println("DB - Insert Fee on Job");
            }


        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            try { rs.close(); } catch (Exception e) { /* Ignored */ }
            try { ps.close(); } catch (Exception e) { /* Ignored */ }
            try { st.close(); } catch (Exception e) { /* Ignored */ }
            try { conn.close(); } catch (Exception e) { /* Ignored */ }
        }
    }

    private static void setJobValues(Job job, PreparedStatement ps, int invoicePaid) throws SQLException {
        ps.setString(1, job.getJobStart().toString().trim());
        ps.setString(2, job.getJobEnd().toString().trim());
        ps.setString(3, job.getJobDesc().trim());
        ps.setLong(4, invoicePaid);
    }


    //Insert new job into DB
    public static void insertJob(Job job){
        try{

            conn = getConnection();
            String query = "INSERT INTO JOB (JOB_CUSTOMER, JOB_START, JOB_END, JOB_DESCRIPTION, JOB_PAID) VALUES (?,?,?,?,?)";
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, job.getCustID());
            ps.setDate(2, Date.valueOf(job.getJobStart()));
            ps.setDate(3, Date.valueOf(job.getJobEnd()));
            ps.setString(4, job.getJobDesc());
            ps.setBoolean(5, job.getInvoicePaid());
            ps.executeUpdate();
            System.out.println("DB - Insert Job into DB");
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                ViewJobPane.lastInsertID = rs.getInt(1);
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
            finally {
            try { rs.close(); } catch (Exception e) { /* Ignored */ }
            try { ps.close(); } catch (Exception e) { /* Ignored */ }
            try { st.close(); } catch (Exception e) { /* Ignored */ }
            try { conn.close(); } catch (Exception e) { /* Ignored */ }
        }

    }

    //Delete single job from DB by id
    public static void deleteJob(int id) {
        try{
            conn = Database.getConnection();
            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "DELETE FROM JOB WHERE JOB_ID=?";
            // create the java statement
            ps = conn.prepareCall(query);
            ps.setInt(1, id);
            // execute query
            ps.executeUpdate();
            System.out.println("DB - Delete Job");
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        finally {
            try { ps.close(); } catch (Exception e) { /* Ignored */ }
            try { conn.close(); } catch (Exception e) { /* Ignored */ }
        }
    }

}
