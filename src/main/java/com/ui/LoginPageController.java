package com.ui;

import com.jdbc.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {

    @FXML TextField usernameTxt;
    @FXML PasswordField passwordTxt;
    @FXML Label invalidLbl;
    @FXML Button loginBtn;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        Scene home_page_scene = new Scene(home_page_parent, 800, 600);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (DBConnect.validateEmployee(usernameTxt.getText(),passwordTxt.getText()))
        {
            app_stage.hide(); //optional
            app_stage.setTitle("LTWS Invoice System Dashboard");
            app_stage.setScene(home_page_scene);
            app_stage.show();
        }
        else
        {
            usernameTxt.clear();
            passwordTxt.clear();
            invalidLbl.setText("Sorry, invalid credentials");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameTxt.setText("kirkh34");
        passwordTxt.setText("");
    }
}
