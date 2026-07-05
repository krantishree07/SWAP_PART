package com.example.swapmart.logincontrol;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class logincontroller {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginBtn;

    @FXML
    private Label messageLabel;

    @FXML
    void initialize() {
        // If you already wired onAction in FXML, you can skip this line
        // loginBtn.setOnAction(this::handleLogin);
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        // Trim spaces to avoid mismatch
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both username and password!");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // Simple validation (replace with DB check later)
        if (username.equalsIgnoreCase("kk") && password.equals("12345")) {
            messageLabel.setText("Login Successful!");
            messageLabel.setStyle("-fx-text-fill: green;");

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/example/swapmart/dashboardxml/dashboardview.fxml"));
                Scene scene = new Scene(loader.load());

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("SwapMart Dashboard");
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
                messageLabel.setText("Error loading dashboard!");
                messageLabel.setStyle("-fx-text-fill: red;");
            }

        } else {
            messageLabel.setText("Invalid username or password!");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
