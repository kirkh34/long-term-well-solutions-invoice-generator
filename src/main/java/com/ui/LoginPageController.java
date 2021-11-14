package com.ui;

import com.jdbc.Database;
import com.ltws.Employee;
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
    public static Employee employeeLoggedIn;
    public static ObservableList<Employee> allEmployees = FXCollections.observableArrayList();

    @FXML TextField usernameTxt;
    @FXML PasswordField passwordTxt;
    @FXML Label invalidLbl;
    @FXML Button loginBtn;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        employeeLoggedIn = Database.validateEmployee(usernameTxt.getText(),passwordTxt.getText());
        if (employeeLoggedIn != null)
        {
            Parent home_page_parent = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
            Scene home_page_scene = new Scene(home_page_parent, 800, 600);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.hide(); //optional
            app_stage.setTitle("LTWS Invoice System Dashboard");
            app_stage.setScene(home_page_scene);
            app_stage.show();
        }
        else
        {
            usernameTxt.clear();
            passwordTxt.clear();
            invalidLbl.setText("Sorry, invalid credentials");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameTxt.setText("admin");
        passwordTxt.setText("password");
        invalidLbl.setText("Test Credentials - Username: admin | password: password");
        allEmployees = Database.queryEmployees();
    }
}
