package com.ui;
import com.ltws.Customer;
import com.ltws.Employee;
import com.ltws.Job;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewJobPane implements Initializable {
    //Information Tab
    @FXML
    Label custNameLbl;
    @FXML
    Label custStreetLbl;
    @FXML
    Label custCityStateZipLbl;
    @FXML
    Label custPhoneLbl;
    @FXML
    Label custEmailLbl;
    @FXML
    Label materialsCostLbl;
    @FXML
    Label laborCostLbl;
    @FXML
    Label feesCostLbl;
    @FXML
    Label totalCostLbl;
    @FXML
    Button viewCustInfoBtn;
    @FXML
    Button downloadInvoiceBtn;
    @FXML
    Button emailInvoiceBtn;
    @FXML
    Button saveInvoiceStatusBtn;
    @FXML
    Button addEmpJobBtn;
    @FXML
    Button removeEmpJobBtn;
    @FXML
    Button saveJobBtn;
    @FXML
    Button cancelJobBtn;
    @FXML
    Button deleteJobBtn;
    @FXML
    ToggleGroup invoiceStatus;
    @FXML
    RadioButton unpaidInvoiceRadio;
    @FXML
    RadioButton paidInvoiceRadio;
    @FXML
    DatePicker jobStartDate;
    @FXML
    DatePicker jobEndDate;
    @FXML
    TextArea jobDescTxtArea;
    @FXML
    ListView empJobListView;
    @FXML
    ComboBox empJobCombo;
    //Materials Tab
    @FXML
    TextField materialsDescTxt;
    @FXML
    TextField materialsPriceTxt;
    @FXML
    Button materialsAddBtn;
    @FXML
    Button materialsRemoveBtn;
    @FXML
    TableView materialsTable;
    @FXML
    TableColumn<Job.Material, String> materialsDescCol;
    @FXML
    TableColumn<Job.Material, Double> materialsPriceCol;
    @FXML
    Label materialsTotalLbl;
    //Labor Tab
    @FXML
    TextField laborDescTxt;
    @FXML
    TextField laborPriceTxt;
    @FXML
    Button laborAddBtn;
    @FXML
    Button laborRemoveBtn;
    @FXML
    TableView laborTable;
    @FXML
    TableColumn<Job.Labor, String> laborDescCol;
    @FXML
    TableColumn<Job.Labor, Double> laborPriceCol;
    @FXML
    Label laborTotalLbl;
    //Fees Tab
    @FXML
    TextField feesDescTxt;
    @FXML
    TextField feesPriceTxt;
    @FXML
    Button feesAddBtn;
    @FXML
    Button feesRemoveBtn;
    @FXML
    TableView feesTable;
    @FXML
    TableColumn<Job.Fee, String> feesDescCol;
    @FXML
    TableColumn<Job.Fee, Double> feesPriceCol;
    @FXML
    Label feesTotalLbl;

    ObservableList<String> empNames = FXCollections.observableArrayList();
    ObservableList<String> empJobList = FXCollections.observableArrayList();
    ObservableList<Job.Material> materialList = FXCollections.observableArrayList();
    ObservableList<Job.Labor> laborList = FXCollections.observableArrayList();
    ObservableList<Job.Fee> feesList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //materialList.add(new Job.Material("Some Wood",33.00));
        //materialList.add(new Job.Material("Red Bricks",100.43));

        //laborList.add(new Job.Labor("Installation",500.40));
        //laborList.add(new Job.Labor("Removal",55.25));

        //feesList.add(new Job.Fee("Assessment Fee",25.00));
        initJob();
        setCustomerInfo();
        initEmpJobListComboBox();
        initEmployeeJobList();
        initTables();
        updateTotals();
    }

    public void initEmployeeJobList() {
        for (Employee emp : LoginPageController.allEmployees) {
            for (int empID : JobsPane.jobSelected.getJobEmployees()) {
           // System.out.println(empID);
                if (emp.getID() == empID) {
                    System.out.println("yes sir");
                    empJobList.add(emp.getFirstName() + " " + emp.getLastName());
                }
            }
        }
        empJobListView.setItems(empJobList);
    }


    public void initJob(){
        materialList = JobsPane.jobSelected.getMaterialOList();
        laborList = JobsPane.jobSelected.getLaborOList();
        feesList = JobsPane.jobSelected.getFeeOList();
        jobDescTxtArea.setText(JobsPane.jobSelected.getJobDesc());
        jobStartDate.setValue(JobsPane.jobSelected.getJobStart());
        jobEndDate.setValue(JobsPane.jobSelected.getJobEnd());
        if(JobsPane.jobSelected.getInvoicePaid()){
            paidInvoiceRadio.setSelected(true);
        } else{
            unpaidInvoiceRadio.setSelected(true);
        }
    }

    public void setCustomerInfo(){
        for (Customer cust : LoginPageController.allCustomers) {
                if (cust.getID() == JobsPane.jobSelected.getCustID()) {
                    custNameLbl.setText(cust.getFirstName() + " " + cust.getLastName());
                    custStreetLbl.setText(cust.getStreet());
                    custCityStateZipLbl.setText(cust.getCity() + ", " + cust.getState() + " " + cust.getZip());
                    custPhoneLbl.setText(String.valueOf(cust.getPhone()));
                    custEmailLbl.setText(cust.getEmail());
                }
        }
    }


    public void initEmpJobListComboBox(){
        //Loop through list of Employees and get first and last name, set to new list
        for(Employee emp : LoginPageController.allEmployees){
            empNames.add(emp.getFirstName() + " " + emp.getLastName());
        }
        //Create Dropdown of Employees
        empJobCombo.setItems(empNames);
        //Set list of employees to ListView
        empJobListView.setItems(empJobList);
        //Custom Button for ComboBox so Prompt text shows after add
        empJobCombo.setButtonCell(new ListCell<String>(){
            protected void updateItem(String item, boolean btl){
                super.updateItem(item, btl);
                if(item != null) {
                    setText(item);
                }else{
                    setText("Select Employee");
                }
            }
        });
    }
    //Set up tables to accept observable lists
    public void initTables(){
        materialsDescCol.setCellValueFactory(new PropertyValueFactory("itemDesc"));
        materialsPriceCol.setCellValueFactory(cellData -> {
            System.out.println(cellData.getValue());
            String price = String.format("%.2f", cellData.getValue().getItemPrice());
            return new ReadOnlyObjectWrapper(price);
        });
        laborDescCol.setCellValueFactory(new PropertyValueFactory("itemDesc"));
        laborPriceCol.setCellValueFactory(cellData -> {
            System.out.println(cellData.getValue());
            String price = String.format("%.2f", cellData.getValue().getItemPrice());
            return new ReadOnlyObjectWrapper(price);
        });
        feesDescCol.setCellValueFactory(new PropertyValueFactory("itemDesc"));
        feesPriceCol.setCellValueFactory(cellData -> {
            System.out.println(cellData.getValue());
            String price = String.format("%.2f", cellData.getValue().getItemPrice());
            return new ReadOnlyObjectWrapper(price);
        });
        materialsTable.setPlaceholder(new Label("No Materials Added"));
        materialsTable.setItems(materialList);
        laborTable.setPlaceholder(new Label("No Labor Added"));
        laborTable.setItems(laborList);
        feesTable.setPlaceholder(new Label("No Fees Added"));
        feesTable.setItems(feesList);
    }

    // Info Tab - Button Action Events
    public void viewCustInfoAction(ActionEvent event) throws IOException {
        CustomersPane.selectedCustomer = LoginPageController.getCustomerByID(JobsPane.jobSelected.getCustID());
        Main.goToPage(event, "viewCustomersPane.fxml","Viewing Customer  " + CustomersPane.selectedCustomer.getFirstName() + " " + CustomersPane.selectedCustomer.getLastName());
    }

    public void downloadInvoiceAction(){}

    public void emailInvoiceAction(){}

    public void saveInvoiceStatusAction(){
        if(unpaidInvoiceRadio.isSelected()){
            System.out.println("Unpaid");
        }else if (paidInvoiceRadio.isSelected()){
            System.out.println("Paid");
        }
    }

    public void addEmpJobAction(){
        if(empJobCombo.getValue() != null && !empJobList.contains((String) empJobCombo.getValue())) {
            empJobList.add((String) empJobCombo.getValue());
        }else if(empJobCombo.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an employee first!");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "Employee already added!");
            alert.showAndWait();
        }
        //Set value null to trigger if statement to reset prompt text
        empJobCombo.setValue(null);
    }

    public void removeEmpJobAction(){
        empJobList.remove(empJobListView.getSelectionModel().getSelectedItem());
    }

    public void saveJobAction(ActionEvent event){
        System.out.println("saved job");
    }

    public void cancelJobAction(ActionEvent event) throws IOException {
        Main.goToDashboard(event, "JobsPane");
    }
    public void deleteJobAction(){}

    // Materials Tab - Button Action Events
    public void materialsAddAction(){
        if(validateFields("materials")) {
            materialList.add(new Job.Material(materialsDescTxt.getText(), Double.parseDouble(materialsPriceTxt.getText())));
            materialsDescTxt.clear();
            materialsPriceTxt.clear();
            updateTotals();
        }
    }

    public void materialsRemoveAction(){
        if(materialsTable.getSelectionModel().getSelectedItem() != null) {
            Job.Material materialSelected = (Job.Material) materialsTable.getSelectionModel().getSelectedItem();
            materialList.removeIf(materialSelected::equals);
            updateTotals();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "You must select an item to delete!");
            alert.showAndWait();
        }
    }

    // Labor Tab - Button Action Events
    public void laborAddAction(){
        if(validateFields("labor")) {
            laborList.add(new Job.Labor(laborDescTxt.getText(), Double.parseDouble(laborPriceTxt.getText())));
            laborDescTxt.clear();
            laborPriceTxt.clear();
            updateTotals();
        }
    }

    public void laborRemoveAction(){
        if(laborTable.getSelectionModel().getSelectedItem() != null) {
            Job.Labor laborSelected = (Job.Labor) laborTable.getSelectionModel().getSelectedItem();
            laborList.removeIf(laborSelected::equals);
            updateTotals();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "You must select an item to delete!");
            alert.showAndWait();
        }
    }

    // Fees Tab - Button Action Events
    public void feesAddAction(){
        if(validateFields("fees")) {
            feesList.add(new Job.Fee(feesDescTxt.getText(), Double.parseDouble(feesPriceTxt.getText())));
            feesDescTxt.clear();
            feesPriceTxt.clear();
            updateTotals();
        }
    }

    public void feesRemoveAction(){
        if(feesTable.getSelectionModel().getSelectedItem() != null) {
            Job.Fee feeSelected = (Job.Fee) feesTable.getSelectionModel().getSelectedItem();
            feesList.removeIf(feeSelected::equals);
            updateTotals();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "You must select an item to delete!");
            alert.showAndWait();
        }
    }

    public void updateTotals(){
        materialsTotalLbl.setText("$"+String.format("%.2f", materialsTotal()));
        laborTotalLbl.setText("$"+String.format("%.2f", laborTotal()));
        feesTotalLbl.setText("$"+String.format("%.2f", feesTotal()));
        materialsCostLbl.setText("$"+String.format("%.2f", materialsTotal()));
        laborCostLbl.setText("$"+String.format("%.2f", laborTotal()));
        feesCostLbl.setText("$"+String.format("%.2f", feesTotal()));
        double total = materialsTotal()+laborTotal()+feesTotal();
        totalCostLbl.setText("$"+String.format("%.2f",total));
    }

    public double materialsTotal(){
        double amount = 0.0;
        for(Job.Material mat : materialList){
           amount += mat.getItemPrice();
        }
        return amount;
    }
    public double laborTotal(){
        double amount = 0.0;
        for(Job.Labor labor : laborList){
            amount += labor.getItemPrice();
        }
        return amount;
    }
    public double feesTotal(){
        double amount = 0.0;
        for(Job.Fee fee : feesList){
            amount += fee.getItemPrice();
        }
        return amount;
    }

    public boolean validateFields(String table){
        boolean isValid = true;
        String message = "";
        String priceMessage = "";
        TextField desc = new TextField();
        TextField price = new TextField();
        if(table.equals("materials")){
            desc = materialsDescTxt;
            price = materialsPriceTxt;
        } else if(table.equals("labor")){
            desc = laborDescTxt;
            price = laborPriceTxt;
        } else if (table.equals("fees")){
            desc = feesDescTxt;
            price = feesPriceTxt;
        }

        try {
            Double.parseDouble(price.getText());
        } catch (NumberFormatException e) {
            priceMessage = "Price must be a number";
            isValid = false;
        }

        if(desc.getText().trim().equals("")) {
            message += "Description cannot be empty! \n";
            isValid = false;
        }
        if(price.getText().trim().equals("")) {
            priceMessage = "Price cannot be empty!";
            isValid = false;
        }

        if(!isValid) {
            message += priceMessage;
            Alert alert = new Alert(Alert.AlertType.ERROR, message);
            alert.showAndWait();
        }
        return isValid;
    }
}
