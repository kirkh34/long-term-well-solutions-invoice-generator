package com.ui;

import com.ltws.Customer;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomersPane implements Initializable {
    @FXML TableView<Customer> tableView;
    @FXML  TableColumn<Customer, String> firstNameCol;
    @FXML  TableColumn<Customer, String> lastNameCol;
    @FXML  TableColumn<Customer, String> emailCol;
    @FXML  TableColumn<Customer, String> phoneCol;
    @FXML  TableColumn<Customer, String> ratingCol;

    public static Customer selectedCustomer;
    public static boolean addingNewCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableView.refresh();

        firstNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("lastName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));
        phoneCol.setCellValueFactory(cellData -> {
                    Long phone = cellData.getValue().getPhone();
                    String formattedPhone = phone.toString().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
                    return new ReadOnlyStringWrapper(formattedPhone);
                });
        ratingCol.setCellValueFactory(cellData -> {
            int rating = cellData.getValue().getRating();
            String ratingStars = "";
            for (int i = 0; i < rating; i++){
                ratingStars += "★";
            }
            return new ReadOnlyStringWrapper(ratingStars);
        });

        tableView.setItems(LoginPageController.allCustomers);
    }
    @FXML private void onViewCustomer(ActionEvent event) throws IOException {

        String btnID = ((Button) event.getSource()).getId();
        String stageTitle = "";
        if (btnID.equals("viewCustomerBtn")) {
            if(selectedCustomer == null)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING, "You must select a customer first!");
                alert.showAndWait();
                return;
            }
            addingNewCustomer = false;
            stageTitle = "Viewing Customer";
        } else if (btnID.equals("addCustomerBtn")) {
            addingNewCustomer = true;
            stageTitle = "Add New Customer";
        }
        Main.goToPage(event, "viewCustomersPane.fxml",stageTitle);
    }

    public void setOnMouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() > 0){
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                selectedCustomer = tableView.getSelectionModel().getSelectedItem();
            }
        }
    }

}