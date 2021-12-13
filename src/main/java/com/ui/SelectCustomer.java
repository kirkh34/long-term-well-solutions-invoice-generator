package com.ui;

import com.ltws.Customer;
import com.ltws.Employee;
import com.ltws.Job;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SelectCustomer implements Initializable {
    @FXML
    ListView custListView;

    ObservableList<String> custNames = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Customer cust : LoginPageController.allCustomers) {
            custNames.add(cust.getFirstName() + " " + cust.getLastName());
            }
        custListView.setItems(custNames);
    }

    public void cancelAction(ActionEvent event) throws IOException {
        Main.goToDashboard(event, "JobsPane");
    }

    public void createJobAction(ActionEvent event) throws IOException{
        if(custListView.getSelectionModel().getSelectedItem() != null) {
            int id = getCustIdbyName(custListView.getSelectionModel().getSelectedItem().toString());
            for (Customer cust : LoginPageController.allCustomers) {
                if (cust.getID() == id) {
                    JobsPane.jobSelected = new Job(id, new ArrayList<Integer>(), LocalDate.now(), LocalDate.now().plusMonths(1), "", false, FXCollections.observableArrayList(), FXCollections.observableArrayList(), FXCollections.observableArrayList());
                    String stageTitle = "Creating Job for " + cust.getFirstName() + " " + cust.getLastName();
                    ViewJobPane.addingJob = true;
                    Main.goToPage(event, "viewJobPane.fxml", stageTitle);
                }
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a customer first!");
            alert.showAndWait();
        }
    }

    public static int getCustIdbyName(String name){
        int id = 0;
        String[] firstAndLast = name.split("\\s+");
        String first = firstAndLast[0].trim();
        String last = firstAndLast[1].trim();
        for (Customer cust : LoginPageController.allCustomers) {
            if(first.equals(cust.getFirstName()) && last.equals(cust.getLastName())){
                id = cust.getID();
            }
        }
        return id;
    }

}
