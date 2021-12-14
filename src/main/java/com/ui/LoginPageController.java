package com.ui;

import com.jdbc.Database;
import com.ltws.Customer;
import com.ltws.Employee;
import com.ltws.Job;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {
    public static String showPane = "";
    public static Employee employeeLoggedIn;
    public static ObservableList<Employee> allEmployees = FXCollections.observableArrayList();
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    public static ObservableList<Job> allJobs = FXCollections.observableArrayList();

    @FXML TextField usernameTxt;
    @FXML PasswordField passwordTxt;
    @FXML Label invalidLbl;
    @FXML Button loginBtn;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        try {
            employeeLoggedIn = Database.validateEmployee(usernameTxt.getText(), passwordTxt.getText());
            if (employeeLoggedIn != null) {
                Parent parent = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                Scene scene = new Scene(parent, 800, 600);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.hide(); //optional
                stage.setTitle("LTWS Invoice System Dashboard");
                stage.setScene(scene);
                stage.show();
            } else {
                throw new LoginException("Invalid Credentials");
            }
        } catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameTxt.setText("admin");
        passwordTxt.setText("password");
        allEmployees = Database.queryEmployees();
        allCustomers = Database.queryCustomers();
        allJobs = Database.queryJobs();
        showPane = "JobsPane";
    }

    public static Customer getCustomerByID(int id){
        Customer customer = null;
        for(Customer cust : allCustomers){
            if(cust.getID() == id) {
                customer = cust;
            }
        }
        return customer;
    }
}

class LoginException extends Exception{
    LoginException(String message){
        super(message);
    }
}
