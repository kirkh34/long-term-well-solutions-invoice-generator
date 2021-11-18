package com.ui;

import com.jdbc.Database;
import com.ltws.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewPayment implements Initializable {
    @FXML Label viewPaymentLbl;
    @FXML TextField ccNumberTxt;
    @FXML TextField ccExpMonthTxt;
    @FXML TextField ccExpYearTxt;
    @FXML TextField ccCVCTxt;
    @FXML TextField ccZipTxt;
    @FXML Button showCCBtn;
    @FXML Button editPaymentBtn;
    @FXML Button savePaymentBtn;
    @FXML Button deletePaymentBtn;


    static boolean empValidated = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!ViewCustomersPane.addingPayment) {
            String ccNum =  CustomersPane.selectedCustomer.getCcNumber() == 0 ? "" : String.valueOf(CustomersPane.selectedCustomer.getCcNumber()).substring(12, 16);
            ccNumberTxt.setText("XXXXXXXXXXXX" + ccNum);
            String exp = String.valueOf(CustomersPane.selectedCustomer.getCcExp());
            String expMonth = exp.substring(0, 2);
            String expYear = exp.substring(2, 4);
            ccExpMonthTxt.setText(expMonth);
            ccExpYearTxt.setText(expYear);
            ccCVCTxt.setText(String.valueOf(CustomersPane.selectedCustomer.getCcCvc()));
            ccZipTxt.setText(String.valueOf(CustomersPane.selectedCustomer.getCcZip()));
        } else{
            ccNumberTxt.setDisable(false);
            ccExpMonthTxt.setDisable(false);
            ccExpYearTxt.setDisable(false);
            ccCVCTxt.setDisable(false);
            ccZipTxt.setDisable(false);
            editPaymentBtn.setVisible(false);
            savePaymentBtn.setVisible(true);
            showCCBtn.setVisible(false);
        }
    }

    public void editPayment(){
        ccNumberTxt.setDisable(false);
        ccExpMonthTxt.setDisable(false);
        ccExpYearTxt.setDisable(false);
        ccCVCTxt.setDisable(false);
        ccZipTxt.setDisable(false);
        editPaymentBtn.setVisible(false);
        savePaymentBtn.setVisible(true);
        deletePaymentBtn.setVisible(true);
    }
    public void savePayment(ActionEvent event) throws IOException{
        CustomersPane.selectedCustomer.setCcNumber(Long.parseLong(ccNumberTxt.getText()));
        int ccExp = Integer.parseInt(ccExpMonthTxt.getText() + ccExpYearTxt.getText());
        CustomersPane.selectedCustomer.setCcExp(ccExp);
        CustomersPane.selectedCustomer.setCcCvc(Integer.parseInt(ccCVCTxt.getText()));
        CustomersPane.selectedCustomer.setCcZip(Integer.parseInt(ccZipTxt.getText()));
        Database.updatePayment(CustomersPane.selectedCustomer);
        cancelBtnAction(event);
    }

    public void deletePayment(ActionEvent event) throws IOException {
        //delete from current selected customer
        CustomersPane.selectedCustomer.setCcNumber(0);
        CustomersPane.selectedCustomer.setCcExp(0);
        CustomersPane.selectedCustomer.setCcCvc(0);
        CustomersPane.selectedCustomer.setCcZip(0);
                for (Customer cust : LoginPageController.allCustomers){
                    if(CustomersPane.selectedCustomer.getID() == cust.getID())
                    {
                        //delete from list of customers object
                        cust.setCcNumber(0);
                        cust.setCcExp(0);
                        cust.setCcCvc(0);
                        cust.setCcZip(0);
                    }
                }
        //Delete from DB
        Database.deletePayment(CustomersPane.selectedCustomer.getID());
        //Go back to previous page
        cancelBtnAction(event);
    }

    public void showCC(){
        if(validate()) {
            ccNumberTxt.setText(String.valueOf(CustomersPane.selectedCustomer.getCcNumber()));
            showCCBtn.setVisible(false);
        }
    }

    public void cancelBtnAction(ActionEvent event) throws IOException {
        String windowTitle = "Viewing  " + CustomersPane.selectedCustomer.getFirstName() + " " + CustomersPane.selectedCustomer.getLastName();
        Main.goToPage(event, "ViewCustomersPane.fxml", windowTitle);
    }
    public boolean validate(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Verify Password");
        window.setMinWidth(200);

        Label label = new Label();
        label.setText("Enter your password to continue");
        PasswordField passField = new PasswordField();
        Button confirmBtn = new Button("Confirm");
        confirmBtn.setDefaultButton(true);
        confirmBtn.setOnAction(e -> {
            if (!passField.getText().equals(LoginPageController.employeeLoggedIn.getPassword())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Password");
                alert.showAndWait();
            } else{
                empValidated = true;
                window.close();
            }
        });
        Button closeBtn = new Button("Cancel");
        closeBtn.setOnAction(e -> {
            empValidated = false;
            window.close();
        });

        VBox layout = new VBox(10);
        Insets insets = new Insets(15,15,15,15);
        HBox bottomRow = new HBox(10);
        bottomRow.getChildren().addAll(confirmBtn, closeBtn);
        bottomRow.setAlignment(Pos.CENTER);
        layout.setPadding(insets);
        layout.getChildren().addAll(label, passField, bottomRow);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return empValidated;
    }
}
