package com.ui;

import com.jdbc.Database;
import com.ltws.Customer;
import com.ltws.Employee;
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

public class ViewCustomersPane implements Initializable {
    @FXML
    Label viewCustomerPaneLbl;
    @FXML
    TextField firstNameTxt;
    @FXML
    TextField lastNameTxt;
    @FXML
    TextField emailTxt;
    @FXML
    TextField phoneTxt;
    @FXML
    TextField streetTxt;
    @FXML
    TextField cityTxt;
    @FXML
    ComboBox stateComboBox;
    @FXML
    TextField zipTxt;
    @FXML
    RadioButton radio1;
    @FXML
    RadioButton radio2;
    @FXML
    RadioButton radio3;
    @FXML
    RadioButton radio4;
    @FXML
    RadioButton radio5;
    @FXML
    TextField wellDepthTxt;
    @FXML
    TextField wellFlowRateTxt;
    @FXML
    TextArea wellLocationTxtArea;
    @FXML
    Button editCustomerBtn;
    @FXML
    Button saveCustomerBtn;
    @FXML
    Button deleteCustomerBtn;

    @FXML Button viewPaymentBtn;
    @FXML Button addPaymentBtn;
    @FXML Label cardEndingLbl;
    @FXML Label paymentInfoLbl;

    @FXML
    ToggleGroup toggleGroup = new ToggleGroup();
    @FXML
    RowConstraints paymentRow;
    @FXML
    RowConstraints emailRow;
    int selectedRadio;
    public Customer customer = CustomersPane.selectedCustomer;
    public static int lastInsertID_Customer;
    public static boolean addingPayment = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFields();
    }

    private void initRadios() {
        radio1.setToggleGroup(toggleGroup);
        radio2.setToggleGroup(toggleGroup);
        radio3.setToggleGroup(toggleGroup);
        radio4.setToggleGroup(toggleGroup);
        radio5.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            RadioButton radioChoice = (RadioButton) newVal.getToggleGroup().getSelectedToggle();
            String radioID = radioChoice.getId();
            if (radioID.equals("radio1")) selectedRadio = 1;
            else if (radioID.equals("radio2")) selectedRadio = 2;
            else if (radioID.equals("radio3")) selectedRadio = 3;
            else if (radioID.equals("radio4")) selectedRadio = 4;
            else if (radioID.equals("radio5")) selectedRadio = 5;
            else selectedRadio = 0;
        });
    }

    private void setRadios(){
        if (customer.getRating() == 1) radio1.setSelected(true);
        if (customer.getRating() == 2) radio2.setSelected(true);
        if (customer.getRating() == 3) radio3.setSelected(true);
        if (customer.getRating() == 4) radio4.setSelected(true);
        if (customer.getRating() == 5) radio5.setSelected(true);
    }


    public void initFields(){
        stateComboBox.setItems(ViewEmployeePane.stateComboOptions);
        initRadios();
        if(customer != null && !CustomersPane.addingNewCustomer) {
            setRadios();
            showPaymentRow();
            viewCustomerPaneLbl.setText("Viewing  " + customer.getFirstName() + " " + customer.getLastName());
            firstNameTxt.setText(customer.getFirstName());
            lastNameTxt.setText(customer.getLastName());
            streetTxt.setText(customer.getStreet());
            cityTxt.setText(customer.getCity());
            stateComboBox.setValue(customer.getState());
            zipTxt.setText(String.valueOf(customer.getZip()));
            phoneTxt.setText(String.valueOf(customer.getPhone()));
            emailTxt.setText(customer.getEmail());
            wellDepthTxt.setText(String.valueOf(customer.getWellDepth()));
            wellFlowRateTxt.setText(customer.getWellFlowRate());
            wellLocationTxtArea.setText(customer.getWellLocation());
            System.out.println(customer.getCcNumber());
            if(customer.getCcNumber() == 0) {
                addPaymentBtn.setVisible(true);
                viewPaymentBtn.setVisible(false);
                cardEndingLbl.setVisible(false);
            }
            else {
                addPaymentBtn.setVisible(false);
                viewPaymentBtn.setVisible(true);
                cardEndingLbl.setText("Card ending in " + String.valueOf(customer.getCcNumber()).substring(12, 16));
            }
        }
        else if(CustomersPane.addingNewCustomer){
            customer = null;
            viewCustomerPaneLbl.setText("Add New Customer");
            radio3.setSelected(true);
            enableFields();
            hidePaymentRow();
            editCustomerBtn.setVisible(false);
            saveCustomerBtn.setVisible(true);
            deleteCustomerBtn.setVisible(false);
         }

        addPaymentBtn.setOnAction(e ->{
            addingPayment = true;
            try {
                Main.goToPage(e,"viewPayment.fxml", "Add Payment");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        viewPaymentBtn.setOnAction(e ->{
            addingPayment = false;
            try {
                Main.goToPage(e,"viewPayment.fxml", "View Payment");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void hidePaymentRow()
    {
        paymentRow.setPrefHeight(0.0);
        viewPaymentBtn.setVisible(false);
        cardEndingLbl.setVisible(false);
        paymentInfoLbl.setVisible(false);
    }
    public void showPaymentRow()
    {
        paymentRow.setPrefHeight(emailRow.getPrefHeight());
        viewPaymentBtn.setVisible(true);
        cardEndingLbl.setVisible(true);
        paymentInfoLbl.setVisible(true);
    }
    public void editCustomer(ActionEvent event) {
        viewCustomerPaneLbl.setText("Editing  " + customer.getFirstName() + " " + customer.getLastName());
        enableFields();
        editCustomerBtn.setVisible(false);
        saveCustomerBtn.setVisible(true);
        deleteCustomerBtn.setVisible(true);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Editing Customer");
    }

    public void saveCustomer(ActionEvent event) throws IOException {
        if(customer != null && !CustomersPane.addingNewCustomer) {
            customer.setFirstName(firstNameTxt.getText());
            customer.setLastName(lastNameTxt.getText());
            customer.setStreet(streetTxt.getText());
            customer.setCity(cityTxt.getText());
            customer.setState((String) stateComboBox.getValue());
            customer.setZip(Integer.parseInt(zipTxt.getText()));
            customer.setPhone(Long.parseLong(phoneTxt.getText()));
            customer.setEmail(emailTxt.getText());
            customer.setRating(selectedRadio);
            customer.setWellDepth(Integer.parseInt(wellDepthTxt.getText()));
            customer.setWellFlowRate(wellFlowRateTxt.getText());
            customer.setWellLocation(wellLocationTxtArea.getText());

            if(validateFields()) {
                Database.updateCustomer(customer);
                customer = null;
                goToDashboard(event);
            }
        } else if(CustomersPane.addingNewCustomer) {
            System.out.println("Selected Radio");
            System.out.println(selectedRadio);
            Customer newCustomer = new Customer(
                    0,
                    firstNameTxt.getText(),
                    lastNameTxt.getText(),
                    streetTxt.getText(),
                    cityTxt.getText(),
                    (String) stateComboBox.getValue(),
                    Integer.parseInt(zipTxt.getText()),
                    Long.parseLong(phoneTxt.getText()),
                    emailTxt.getText(),
                    selectedRadio,
                    Integer.parseInt(wellDepthTxt.getText()),
                    wellFlowRateTxt.getText(),
                    wellLocationTxtArea.getText(),
                    0L,
                    0,
                    0,
                    0
            );
            if(validateFields()) {
                Database.insertCustomer(newCustomer);
                newCustomer.setID(lastInsertID_Customer);
                LoginPageController.allCustomers.add(newCustomer);
                Main.goToPage(event,"dashboard.fxml", "LTWS Invoice System Dashbaord");
            }
        }
    }

    public void deleteCustomer(ActionEvent event) throws IOException {
        Database.deleteCustomer(customer.getID());
        LoginPageController.allCustomers.removeIf(e -> e.equals(customer));
        customer = null;
        goToDashboard(event);
    }


    public void goToDashboard(ActionEvent event) throws IOException {
        CustomersPane.selectedCustomer = null;
        Main.goToPage(event,"dashboard.fxml", "LTWS Invoice System Dashboard");
        LoginPageController.showPane = "CustomersPane";
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
        emailTxt.setDisable(false);
        wellDepthTxt.setDisable(false);
        wellFlowRateTxt.setDisable(false);
        wellLocationTxtArea.setDisable(false);
        radio1.setDisable(false);
        radio2.setDisable(false);
        radio3.setDisable(false);
        radio4.setDisable(false);
        radio5.setDisable(false);
    }

    public boolean validateFields(){

        String message = "";
        if (zipTxt.getText().length() != 5) message += "Zip Code must be be 5 digits \n";
        if (phoneTxt.getText().length() != 10) message += "Phone number must be 10 digits \n";
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
