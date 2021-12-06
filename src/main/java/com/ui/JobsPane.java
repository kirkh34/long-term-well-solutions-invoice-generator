package com.ui;
import com.ltws.Job;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
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

        viewJobsTable.setItems(LoginPageController.allJobs);
    }

    @FXML public void viewJobAction(ActionEvent event) throws IOException {
        if(viewJobsTable.getSelectionModel().getSelectedItem() != null) {
            jobSelected = (Job) viewJobsTable.getSelectionModel().getSelectedItem();
            String stageTitle = "Viewing Job #" + jobSelected.getID();
            Main.goToPage(event, "viewJobPane.fxml", stageTitle);
        }
    }

    @FXML public void addJobAction(){

    }
}
