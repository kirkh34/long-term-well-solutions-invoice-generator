package com.ui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewJobPane implements Initializable {
    //Information Tab
    @FXML Label custNameLbl;
    @FXML Label custStreetLbl;
    @FXML Label custCityStateZipLbl;
    @FXML Label custPhoneLbl;
    @FXML Label custEmailLbl;
    @FXML Label materialsCostLbl;
    @FXML Label laborCostLbl;
    @FXML Label feesCostLbl;
    @FXML Label totalCostLbl;
    @FXML Button viewCustInfoBtn;
    @FXML Button downloadInvoiceBtn;
    @FXML Button emailInvoiceBtn;
    @FXML Button saveInvoiceStatusBtn;
    @FXML Button addEmpJobBtn;
    @FXML Button removeEmpJobBtn;
    @FXML Button saveJobBtn;
    @FXML Button cancelJobBtn;
    @FXML Button deleteJobBtn;
    @FXML ToggleGroup invoiceStatus;
    @FXML RadioButton unpaidInvoiceRadio;
    @FXML RadioButton paidInvoiceRadio;
    @FXML DatePicker jobStartDate;
    @FXML DatePicker jobEndDate;
    @FXML TextArea jobDescTxtArea;
    @FXML ListView  empJobList;
    @FXML ComboBox empJobCombo;
    //Materials Tab
    @FXML TextField materialsDescTxt;
    @FXML TextField materialsPriceTxt;
    @FXML Button materialsAddBtn;
    @FXML Button materialsDeleteBtn;
    @FXML TableView materialsTable;
    @FXML Label materialsTotalLbl;
    //Labor Tab
    @FXML TextField laborDescTxt;
    @FXML TextField laborPriceTxt;
    @FXML Button laborAddBtn;
    @FXML Button laborDeleteBtn;
    @FXML TableView laborTable;
    @FXML Label laborTotalLbl;
    //Fees Tab
    @FXML TextField feesDescTxt;
    @FXML TextField feesPriceTxt;
    @FXML Button feesAddBtn;
    @FXML Button feesDeleteBtn;
    @FXML TableView feesTable;
    @FXML Label feesTotalLbl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void cancelJobAction(ActionEvent event) throws IOException {
        Main.goToDashboard(event, "JobsPane");
    }

    public void saveJobAction(ActionEvent event){
        System.out.println("saved job");
    }

}
