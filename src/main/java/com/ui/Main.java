package com.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Please Login Below");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void goToPage(ActionEvent event, String fxmlPage, String title) throws IOException {
        Parent parent = FXMLLoader.load(Main.class.getResource(fxmlPage));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide(); //optional
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

        public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        public static boolean validateEmail(String emailStr) {
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
            return matcher.find();
        }

    public static void main(String[] args) {
        launch();
    }
}