package com.ui;
import com.jdbc.Database;
import com.ltws.Customer;
import com.ltws.Employee;
import com.ltws.Job;
import com.ltws.Person;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.ltws.GenerateInvoice;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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

    public static boolean addingJob;
    public static int lastInsertID;
    public Customer jobCustomer;
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
        if(addingJob){
            deleteJobBtn.setVisible(false);
        } else{
            deleteJobBtn.setVisible(true);
        }
    }

    public void initEmployeeJobList() {
        for (Employee emp : LoginPageController.allEmployees) {
            for (int empID : JobsPane.jobSelected.getJobEmployees()) {
            System.out.println(empID);
                if (emp.getID() == empID) {
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
                    jobCustomer = cust;
                    custNameLbl.setText(cust.getFirstName() + " " + cust.getLastName());
                    custStreetLbl.setText(cust.getStreet());
                    custCityStateZipLbl.setText(cust.getCity() + ", " + cust.getState() + " " + cust.getZip());
                    custPhoneLbl.setText(String.valueOf(cust.getPhone()).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3"));
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
        ViewCustomersPane.comingFromJob = true;
        Main.goToPage(event, "viewCustomersPane.fxml","Viewing Customer  " + CustomersPane.selectedCustomer.getFirstName() + " " + CustomersPane.selectedCustomer.getLastName());
    }
    boolean running = false;
    public void updateJobDesc(){
        if(!running) {
            running = true;
            CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS).execute(() -> {
                JobsPane.jobSelected.setJobDesc(jobDescTxtArea.getText());
                System.out.println(jobDescTxtArea.getText());
                running = false;
            });
        }
    }

    public void toggleUnpaidRadio(){
        JobsPane.jobSelected.setInvoicePaid(false);
    }

    public void togglePaidRadio(){
        JobsPane.jobSelected.setInvoicePaid(true);
    }

    public void downloadInvoiceAction() throws Exception {
        if(JobsPane.jobSelected.getMaterialOList().isEmpty() && JobsPane.jobSelected.getLaborOList().isEmpty() && JobsPane.jobSelected.getFeeOList().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Unable to generate invoice.  Please add either materials, labor, or fees to job");
            alert.showAndWait();
        }else {
            double total = materialsTotal() + laborTotal() + feesTotal();
            Person company = new Person(1, "Long Term", "Well Solutions", "ltwsinvoices@gmail.com", "123 Easy Street", "Crawfordsville", "IN", 47933, 7653654362L);
            GenerateInvoice invoice = new GenerateInvoice(JobsPane.jobSelected, jobCustomer, company, total);
            invoice.download();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invoice was saved to your downloads folder");
            alert.showAndWait();
        }
    }

    public void emailInvoiceAction(){
        if(JobsPane.jobSelected.getMaterialOList().isEmpty() && JobsPane.jobSelected.getLaborOList().isEmpty() && JobsPane.jobSelected.getFeeOList().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Unable to email invoice.  Please add either materials, labor, or fees to job");
            alert.showAndWait();
        }else {
            double total = materialsTotal() + laborTotal() + feesTotal();
            Person company = new Person(1, "Long Term", "Well Solutions", "ltwsinvoices@gmail.com", "123 Easy Street", "Crawfordsville", "IN", 47933, 7653654362L);
            GenerateInvoice invoice = new GenerateInvoice(JobsPane.jobSelected, jobCustomer, company, total);
            invoice.email(jobCustomer.getEmail());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invoice was emailed to the customer");
            alert.showAndWait();
        }
    }

    public void onChangeStartDate(){
        JobsPane.jobSelected.setJobStart(jobStartDate.getValue());
    }

    public void onChangeEndDate(){
        JobsPane.jobSelected.setJobEnd(jobEndDate.getValue());
    }

    public void addEmpJobAction(){
        if(empJobCombo.getValue() != null && !empJobList.contains((String) empJobCombo.getValue())) {
            empJobList.add((String) empJobCombo.getValue());
            int id = getEmpIdbyName((String) empJobCombo.getValue());
            JobsPane.jobSelected.jobEmployees.add(id);
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
        int id = getEmpIdbyName(empJobListView.getSelectionModel().getSelectedItem().toString());
        JobsPane.jobSelected.jobEmployees.remove(Integer.valueOf(id));
        empJobList.remove(empJobListView.getSelectionModel().getSelectedItem());
    }

    public static int getEmpIdbyName(String name){
        int id = 0;
        String[] firstAndLast = name.split("\\s+");
        String first = firstAndLast[0].trim();
        String last = firstAndLast[1].trim();
        for (Employee emp : LoginPageController.allEmployees) {
            if(first.equals(emp.getFirstName()) && last.equals(emp.getLastName())){
                id = emp.getID();
            }
        }
        return id;
    }

    public void saveJobAction(ActionEvent event){
        if(addingJob){
            Database.insertJob(JobsPane.jobSelected);
            JobsPane.jobSelected.setID(lastInsertID);
            LoginPageController.allJobs.add(JobsPane.jobSelected);
            deleteJobBtn.setVisible(true);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Viewing Job for " + JobsPane.jobSelected.getCustName());
            addingJob = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Job was created");
            alert.showAndWait();
        } else {
            Database.updateJob(JobsPane.jobSelected);

            Job oldJob = null;
            for(Job job : LoginPageController.allJobs){
                if(job.getID() == JobsPane.jobSelected.getID()) oldJob = job;
            }
            LoginPageController.allJobs.remove(oldJob);
            LoginPageController.allJobs.add(JobsPane.jobSelected);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Job was saved");
            alert.showAndWait();
        }

    }

    public void cancelJobAction(ActionEvent event) throws IOException {
        JobsPane.jobSelected = null;
        Main.goToDashboard(event, "JobsPane");
    }
    public void deleteJobAction(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this job?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK) {
            Database.deleteJob(JobsPane.jobSelected.getID());
            Job oldJob = null;
            for(Job job : LoginPageController.allJobs){
                if(job.getID() == JobsPane.jobSelected.getID()) oldJob = job;
            }
            LoginPageController.allJobs.remove(oldJob);
            JobsPane.jobSelected = null;
            Main.goToDashboard(event, "JobsPane");
        }
    }

    // Materials Tab - Button Action Events
    public void materialsAddAction(){
        if(validateFields("materials")) {
            materialList.add(new Job.Material(materialsDescTxt.getText(), Double.parseDouble(materialsPriceTxt.getText())));
            materialsDescTxt.clear();
            materialsPriceTxt.clear();
            updateTotals();
            JobsPane.jobSelected.setMaterialOList(materialList);
        }
    }

    public void materialsRemoveAction(){
        if(materialsTable.getSelectionModel().getSelectedItem() != null) {
            Job.Material materialSelected = (Job.Material) materialsTable.getSelectionModel().getSelectedItem();
            materialList.removeIf(materialSelected::equals);
            updateTotals();
            JobsPane.jobSelected.setMaterialOList(materialList);
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
            JobsPane.jobSelected.setLaborOList(laborList);
        }
    }

    public void laborRemoveAction(){
        if(laborTable.getSelectionModel().getSelectedItem() != null) {
            Job.Labor laborSelected = (Job.Labor) laborTable.getSelectionModel().getSelectedItem();
            laborList.removeIf(laborSelected::equals);
            updateTotals();
            JobsPane.jobSelected.setLaborOList(laborList);
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
            JobsPane.jobSelected.setFeeOList(feesList);
        }
    }

    public void feesRemoveAction(){
        if(feesTable.getSelectionModel().getSelectedItem() != null) {
            Job.Fee feeSelected = (Job.Fee) feesTable.getSelectionModel().getSelectedItem();
            feesList.removeIf(feeSelected::equals);
            updateTotals();
            JobsPane.jobSelected.setFeeOList(feesList);
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
