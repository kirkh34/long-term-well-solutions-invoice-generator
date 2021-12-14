package com.ui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    @FXML BorderPane borderPane;
    @FXML Button jobsPane;
    @FXML Button customersPane;
    @FXML Button employeesPane;

    @FXML Label welcomeLbl;

    @FXML
    private void changeContent(ActionEvent event) throws IOException {
        String btnID = ((Button) event.getSource()).getId();
        URL url = getClass().getResource(btnID+ ".fxml");
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load(url);
        borderPane.setCenter(root);
        changeBtnColor(btnID);
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        Parent loginPage = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
        Scene loginScene = new Scene(loginPage);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide(); //optional
        app_stage.setTitle("Please Login Below");
        app_stage.setScene(loginScene);
        app_stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLbl.setText("Welcome, " + LoginPageController.employeeLoggedIn.getFirstName());
        System.out.println(LoginPageController.showPane);
        if(LoginPageController.showPane.equals("EmployeesPane")) employeesPane.fire();
        if(LoginPageController.showPane.equals("CustomersPane")) customersPane.fire();
        if(LoginPageController.showPane.equals("JobsPane")) jobsPane.fire();
        if(!LoginPageController.employeeLoggedIn.getIsAdmin()){
            employeesPane.setVisible(false);
        }
    }

    public void changeBtnColor(String btnID){
        if(btnID.equals("jobsPane")) {
            jobsPane.setStyle("-fx-background-color: #555;");
            employeesPane.setStyle("-fx-background-color: #333;");
            customersPane.setStyle("-fx-background-color: #333;");
        } else if(btnID.equals("customersPane")) {
            customersPane.setStyle("-fx-background-color: #555;");
            jobsPane.setStyle("-fx-background-color: #333;");
            employeesPane.setStyle("-fx-background-color: #333;");
        } else if(btnID.equals("employeesPane")) {
            employeesPane.setStyle("-fx-background-color: #555;");
            jobsPane.setStyle("-fx-background-color: #333;");
            customersPane.setStyle("-fx-background-color: #333;");
        }
    }
}
