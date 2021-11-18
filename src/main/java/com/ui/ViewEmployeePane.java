package com.ui;

import com.jdbc.Database;
import com.ltws.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewEmployeePane implements Initializable {
    public Employee employee = EmployeesPane.selectedEmployee;
    public static int lastInsertID;
    @FXML Label viewEmployeePaneLbl;
    @FXML TextField firstNameTxt;
    @FXML TextField lastNameTxt;
    @FXML TextField emailTxt;
    @FXML TextField streetTxt;
    @FXML TextField cityTxt;
    @FXML ComboBox stateComboBox;
    @FXML TextField zipTxt;
    @FXML TextField phoneTxt;
    @FXML Label empIDLbl;
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
    @FXML RowConstraints empIDRow;
    @FXML RowConstraints phoneRow;

    public static ObservableList<String> stateComboOptions = FXCollections.observableArrayList("AK", "AL", "AR", "AS", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "GU", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VI", "VT", "WA", "WI", "WV", "WY");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stateComboBox.setItems(stateComboOptions);

        if(employee != null && !EmployeesPane.addingNewEmployee) {
            showEmpIDRow();
            viewEmployeePaneLbl.setText("Viewing  " + employee.getFirstName() + " " + employee.getLastName());
            firstNameTxt.setText(employee.getFirstName());
            lastNameTxt.setText(employee.getLastName());
            streetTxt.setText(employee.getStreet());
            emailTxt.setText(employee.getEmail());
            cityTxt.setText(employee.getCity());
            stateComboBox.setValue(employee.getState());
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
            hideEmpIDRow();
            viewEmployeePaneLbl.setText("Add New Employee");
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
        stateComboBox.setDisable(false);
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
        LoginPageController.showPane = "EmployeesPane";
        Main.goToPage(event,"dashboard.fxml", "LTWS Invoice System Dashboard");
    }

    public void editEmployee(ActionEvent event){
        viewEmployeePaneLbl.setText("Editing  " + employee.getFirstName() + " " + employee.getLastName());
        enableFields();

        editBtn.setVisible(false);
        saveBtn.setVisible(true);
        deleteBtn.setVisible(true);
        adminToggle.setVisible(true);
        adminLbl.setVisible(false);

        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Editing Employee");

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
            employee.setState((String) stateComboBox.getValue());
            employee.setZip(Integer.parseInt(zipTxt.getText()));
            employee.setPhone(Long.parseLong(phoneTxt.getText()));
            employee.setDln(Long.parseLong(driversLicenseTxt.getText()));
            employee.setSsn(Integer.parseInt(ssnTxt.getText()));
            employee.setUsername(usernameTxt.getText());
            employee.setPassword(passwordTxt.getText());
            employee.setAdmin(adminToggle.isSelected());

            if(validateFields()) {
                Database.updateEmployee(employee);
                employee = null;
                Main.goToPage(event,"dashboard.fxml", "LTWS Invoice System Dashboard");
            }
        } else if(EmployeesPane.addingNewEmployee) {

            Employee newEmployee = new Employee(
                0,
                firstNameTxt.getText(),
                lastNameTxt.getText(),
                emailTxt.getText(),
                streetTxt.getText(),
                cityTxt.getText(),
                (String) stateComboBox.getValue(),
                Integer.parseInt(zipTxt.getText()),
                Long.parseLong(phoneTxt.getText()),
                Integer.parseInt(ssnTxt.getText()),
                Long.parseLong(driversLicenseTxt.getText()),
                usernameTxt.getText(),
                passwordTxt.getText(),
                adminToggle.isSelected()
            );
            if(validateFields()) {
                Database.insertEmployee(newEmployee);
                newEmployee.setID(lastInsertID);
                LoginPageController.allEmployees.add(newEmployee);
                Main.goToPage(event,"dashboard.fxml", "LTWS Invoice System Dashboard");
            }
        }
    }

    public void deleteEmployee(ActionEvent event) throws IOException {
        Database.deleteEmployee(employee.getID());
        LoginPageController.allEmployees.removeIf(e -> e.equals(employee));
        employee = null;
        Main.goToPage(event,"dashboard.fxml", "LTWS Invoice System Dashboard");
    }

    public void hideEmpIDRow()
    {
        empIDRow.setPrefHeight(0.0);
        empIDLbl.setVisible(false);
        empIDTxt.setVisible(false);
    }
    public void showEmpIDRow()
    {
        empIDRow.setPrefHeight(phoneRow.getPrefHeight());
        empIDLbl.setVisible(true);
        empIDTxt.setVisible(true);
    }

    public boolean validateFields(){

        String message = "";
        if (zipTxt.getText().length() != 5) message += "Zip Code must be be 5 digits \n";
        if (phoneTxt.getText().length() != 10) message += "Phone number must be 10 digits \n";
        if (driversLicenseTxt.getText().length() != 10) message += "Driver's license number must be 10 digits \n";
        if (ssnTxt.getText().length() != 9) message += "Social security number must be 9 digits \n";
        if (!Main.validateEmail(emailTxt.getText())) message += "Email address is invalid";
        System.out.println();

        if (message == "")
            return true;
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, message);
            alert.showAndWait();
            return false;
        }
    }



}
