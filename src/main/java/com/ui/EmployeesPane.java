package com.ui;

import com.jdbc.DBConnect;
import com.ltws.Employee;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeesPane implements Initializable {
    @FXML TableView<Employee> tableView;
    @FXML TableColumn<Employee, Integer> IDcol;
    @FXML  TableColumn<Employee, String> firstNameCol;
    @FXML  TableColumn<Employee, String> lastNameCol;
    @FXML  TableColumn<Employee, String> usernameCol;
    @FXML  TableColumn<Employee, String> emailCol;
    @FXML  TableColumn<Employee, String> isAdminCol;

    public static Employee selectedEmployee;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        IDcol.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("ID"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));
        isAdminCol.setCellValueFactory(cellData ->
                {
                boolean isAdmin = cellData.getValue().getIsAdmin();
                String admin = isAdmin ? "Yes" : "No";
                return new ReadOnlyStringWrapper(admin);
                });


        tableView.setItems(DBConnect.queryEmployees());
    }
    @FXML
    private void onViewEmployee(ActionEvent event) throws IOException {
        Parent viewEmployeePane = FXMLLoader.load(getClass().getResource("viewEmployeePane.fxml"));
        Scene viewEmployeeScene = new Scene(viewEmployeePane);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide(); //optional
        app_stage.setTitle("Viewing "+ selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName());
        app_stage.setScene(viewEmployeeScene);
        app_stage.show();
    }
    public void setOnMouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() > 0){
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                selectedEmployee = tableView.getSelectionModel().getSelectedItem();
                System.out.println(selectedEmployee.getEmail());
                //nameTextField.setText(selectedPerson.getName());
                //addressTextField.setText(selectedPerson.getAddress());
            }
        }
    }

}