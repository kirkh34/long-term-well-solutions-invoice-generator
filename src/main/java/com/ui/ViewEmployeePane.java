package com.ui;

import com.jdbc.Database;
import com.ltws.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewEmployeePane implements Initializable {
    public Employee employee = EmployeesPane.selectedEmployee;
    public static int lastInsertID;

    @FXML TextField firstNameTxt;
    @FXML TextField lastNameTxt;
    @FXML TextField emailTxt;
    @FXML TextField streetTxt;
    @FXML TextField cityTxt;
    @FXML TextField stateTxt;
    @FXML TextField zipTxt;
    @FXML TextField phoneTxt;
    @FXML TextField empIDTxt;
    @FXML TextField driversLicenseTxt;
    @FXML TextField ssnTxt;
    @FXML TextField usernameTxt;
    @FXML TextField passwordTxt;
    @FXML TextField isAdminTxt;
    @FXML Button editBtn;
    @FXML Button saveBtn;
    @FXML Button deleteBtn;
    @FXML ToggleButton adminToggle;
    @FXML Label adminLbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(employee != null && !EmployeesPane.addingNewEmployee) {
            firstNameTxt.setText(employee.getFirstName());
            lastNameTxt.setText(employee.getLastName());
            streetTxt.setText(employee.getStreet());
            emailTxt.setText(employee.getEmail());
            cityTxt.setText(employee.getCity());
            stateTxt.setText(employee.getState());
            zipTxt.setText(String.valueOf(employee.getZip()));
            phoneTxt.setText(String.valueOf(employee.getPhone()));
            empIDTxt.setText(String.format("%05d", employee.getID()));
            driversLicenseTxt.setText(String.valueOf(employee.getDln()));
            ssnTxt.setText(String.valueOf(employee.getSsn()));
            usernameTxt.setText(employee.getUsername());
            passwordTxt.setText(employee.getPassword());
            isAdminTxt.setText(employee.getIsAdmin() ? "Yes" : "No");
            adminToggle.setSelected(employee.getIsAdmin());
        }
        else if(EmployeesPane.addingNewEmployee){
            enableFields();
            editBtn.setVisible(false);
            saveBtn.setVisible(true);
            deleteBtn.setVisible(false);
            adminToggle.setVisible(true);
            adminLbl.setVisible(false);
            adminToggle.setSelected(false);
            isAdminTxt.setText("No");
        }
    }

    private void enableFields() {
        firstNameTxt.setDisable(false);
        lastNameTxt.setDisable(false);
        streetTxt.setDisable(false);
        emailTxt.setDisable(false);
        cityTxt.setDisable(false);
        stateTxt.setDisable(false);
        zipTxt.setDisable(false);
        phoneTxt.setDisable(false);
        empIDTxt.setDisable(false);
        driversLicenseTxt.setDisable(false);
        ssnTxt.setDisable(false);
        usernameTxt.setDisable(false);
        passwordTxt.setDisable(false);
        isAdminTxt.setDisable(false);
    }

    public void goToEmployeesPane(ActionEvent event) throws IOException {
        EmployeesPane.selectedEmployee = null;
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("LTWS Invoice System Dashboard");
        MainApplication.goToPage(event,"dashboard.fxml");
    }

    public void editEmployee(ActionEvent event){
        enableFields();

        editBtn.setVisible(false);
        saveBtn.setVisible(true);
        deleteBtn.setVisible(true);
        adminToggle.setVisible(true);
        adminLbl.setVisible(false);

        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Editing "+ employee.getFirstName() + " " + employee.getLastName());

    }

    public void toggleAdmin() {
        isAdminTxt.setText(adminToggle.isSelected() ? "Yes" : "No");
        if(!EmployeesPane.addingNewEmployee) employee.setAdmin(adminToggle.isSelected());
    }

    public void saveEmployee(ActionEvent event) throws IOException {
        if(employee != null && !EmployeesPane.addingNewEmployee) {
            employee.setFirstName(firstNameTxt.getText());
            employee.setLastName(lastNameTxt.getText());
            employee.setStreet(streetTxt.getText());
            employee.setEmail(emailTxt.getText());
            employee.setCity(cityTxt.getText());
            employee.setState(stateTxt.getText());
            employee.setZip(Integer.parseInt(zipTxt.getText()));
            employee.setPhone(Long.parseLong(phoneTxt.getText()));
            employee.setDln(Long.parseLong(driversLicenseTxt.getText()));
            employee.setSsn(Integer.parseInt(ssnTxt.getText()));
            employee.setUsername(usernameTxt.getText());
            employee.setPassword(passwordTxt.getText());
            employee.setAdmin(adminToggle.isSelected());

            Database.updateEmployee(employee);
            employee = null;
        } else if(EmployeesPane.addingNewEmployee) {

            Employee newEmployee = new Employee(
                0,
                firstNameTxt.getText(),
                lastNameTxt.getText(),
                emailTxt.getText(),
                streetTxt.getText(),
                cityTxt.getText(),
                stateTxt.getText(),
                Integer.parseInt(zipTxt.getText()),
                Integer.parseInt(phoneTxt.getText()),
                Integer.parseInt(driversLicenseTxt.getText()),
                Integer.parseInt(ssnTxt.getText()),
                usernameTxt.getText(),
                passwordTxt.getText(),
                adminToggle.isSelected()
            );
            Database.insertEmployee(newEmployee);
            newEmployee.setID(lastInsertID);
            LoginPageController.allEmployees.add(newEmployee);
        }
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("LTWS Invoice System Dashboard");
        MainApplication.goToPage(event,"dashboard.fxml");

    }

    public void deleteEmployee(ActionEvent event) throws IOException {
        Database.deleteEmployee(employee.getID());
        LoginPageController.allEmployees.removeIf(e -> e.equals(employee));
        employee = null;
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("LTWS Invoice System Dashboard");
        MainApplication.goToPage(event,"dashboard.fxml");
    }
}
