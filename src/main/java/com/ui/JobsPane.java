package com.ui;
import com.ltws.Job;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class JobsPane implements Initializable {
    @FXML TableView viewJobsTable;
    @FXML TableColumn<Job, Integer> jobIdCol;
    @FXML TableColumn<Job, String> jobCustCol;
    @FXML TableColumn<Job, String> jobDescCol;
    @FXML TableColumn<Job, String> jobStartDateCol;
    @FXML TableColumn<Job, String> jobEndDateCol;
    @FXML TableColumn<Job, String> jobInvoicedCol;
    @FXML TableColumn<Job, String> jobInvoicePaidCol;

    @FXML Button viewJobBtn;
    @FXML Button addJobBtn;

    public static Job jobSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
    }

    public void initTable(){
        viewJobsTable.setPlaceholder(new Label("No Jobs Added"));
        viewJobsTable.refresh();
        jobIdCol.setCellValueFactory(new PropertyValueFactory("ID"));
        jobCustCol.setCellValueFactory(new PropertyValueFactory("custName"));
        jobDescCol.setCellValueFactory(new PropertyValueFactory("jobDesc"));
        jobStartDateCol.setCellValueFactory(new PropertyValueFactory("jobStart"));
        jobEndDateCol.setCellValueFactory(new PropertyValueFactory("jobEnd"));
        jobInvoicePaidCol.setCellValueFactory(cellData ->
        {
            boolean isPaid = cellData.getValue().getInvoicePaid();
            String paid = isPaid ? "Yes" : "No";
            return new ReadOnlyStringWrapper(paid);
        });
        System.out.println("job list size" + LoginPageController.allJobs.size());
        viewJobsTable.setItems(LoginPageController.allJobs);
        viewJobsTable.getSortOrder().setAll(jobIdCol);
    }

    @FXML public void viewJobAction(ActionEvent event) throws IOException {
        if(viewJobsTable.getSelectionModel().getSelectedItem() != null) {
            Job j = (Job) viewJobsTable.getSelectionModel().getSelectedItem();
            ArrayList<Integer> jEmpList = new ArrayList<>();
            ObservableList<Job.Material> materialList = FXCollections.observableArrayList();
            ObservableList<Job.Labor> laborList = FXCollections.observableArrayList();
            ObservableList<Job.Fee> feeList = FXCollections.observableArrayList();
            for(int id : j.getJobEmployees()){
                jEmpList.add(id);
            }
            for(Job.Material mat: j.getMaterialOList()){
                materialList.add(mat);
            }
            for(Job.Labor labor: j.getLaborOList()){
                laborList.add(labor);
            }
            for(Job.Fee fee: j.getFeeOList()){
                feeList.add(fee);
            }
            jobSelected = new Job(j.getID(), j.getCustID(), jEmpList, j.getJobStart(), j.getJobEnd(), j.getJobDesc(), j.getInvoicePaid(), materialList, laborList, feeList);
            ViewJobPane.addingJob = false;
            String stageTitle = "Viewing Job #" + jobSelected.getID();
            Main.goToPage(event, "viewJobPane.fxml", stageTitle);
        } else{
                Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a job first!");
                alert.showAndWait();
        }
    }

    @FXML public void addJobAction(ActionEvent event) throws IOException{
        String stageTitle = "Select Customer";
        Main.goToPage(event, "selectCustomer.fxml", stageTitle);
    }
}
