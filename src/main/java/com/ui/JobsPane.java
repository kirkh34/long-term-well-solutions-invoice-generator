package com.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JobsPane implements Initializable {
    @FXML Button viewJobBtn;
    @FXML Button addJobBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML public void viewJobAction(ActionEvent event) throws IOException {
        String stageTitle = "Viewing Job";
        Main.goToPage(event, "viewJobPane.fxml", stageTitle);
    }

    @FXML public void addJobAction(){

    }
}
