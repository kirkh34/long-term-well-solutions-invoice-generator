package com.ui;

import com.jdbc.DBConnect;
import com.ltws.Employee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainApplicationController implements Initializable {

    @FXML  TableView<Employee> tableView;
    @FXML  TableColumn<Employee, Integer> IDcol;
    @FXML  TableColumn<Employee, String> firstNameCol;
    @FXML  TableColumn<Employee, String> lastNameCol;
    @FXML  TableColumn<Employee, String> emailCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        IDcol.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("ID"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));

        tableView.setItems(DBConnect.queryEmployees());
    }
}