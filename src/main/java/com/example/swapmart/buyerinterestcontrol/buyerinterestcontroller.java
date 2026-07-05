package com.example.swapmart.buyerinterestcontrol;

import com.example.swapmart.usercontrol.mysqlconnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class buyerinterestcontroller {

    @FXML private TextField idtxtx;
    @FXML private TextField nameField1;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextArea messageField;

    private Connection con;

    @FXML
    void initialize() {
        con = mysqlconnection.getConnection();

        // 🔥 Auto-load user data when typing ID
        idtxtx.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && newVal.matches("\\d+")) {
                loadUserData(Integer.parseInt(newVal));
            }
        });
    }

    // 🔥 USER DATA LOAD (users table)
    private void loadUserData(int userId) {
        String sql = "SELECT name, phone FROM users WHERE id=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                nameField1.setText(rs.getString("name"));
                phoneField.setText(rs.getString("phone"));
            } else {
                nameField1.clear();
                phoneField.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 SUBMIT DATA (INSERT)
    @FXML
    void handleSubmit(ActionEvent event) {
        if (idtxtx.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter User ID first.");
            return;
        }

        String sql = "INSERT INTO buyer_interest (id, name, phone, message, address) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, Integer.parseInt(idtxtx.getText()));
            pst.setString(2, nameField1.getText());
            pst.setString(3, phoneField.getText());
            pst.setString(4, messageField.getText());
            pst.setString(5, addressField.getText());

            pst.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Interest Submitted Successfully!");
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error submitting interest: " + e.getMessage());
        }
    }

    // 🔍 SEARCH
    @FXML
    void handleSearch(ActionEvent event) {
        if (idtxtx.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Enter User ID to search.");
            return;
        }

        String sql = "SELECT * FROM buyer_interest WHERE id=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, Integer.parseInt(idtxtx.getText()));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                nameField1.setText(rs.getString("name"));
                phoneField.setText(rs.getString("phone"));
                messageField.setText(rs.getString("message"));
                addressField.setText(rs.getString("address"));
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No interest found for this User ID.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error searching: " + e.getMessage());
        }
    }

    // ✏ UPDATE
    @FXML
    void handleUpdate(ActionEvent event) {
        if (idtxtx.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Enter User ID to update.");
            return;
        }

        String sql = "UPDATE buyer_interest SET name=?, phone=?, message=?, address=? WHERE id=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nameField1.getText());
            pst.setString(2, phoneField.getText());
            pst.setString(3, messageField.getText());
            pst.setString(4, addressField.getText());
            pst.setInt(5, Integer.parseInt(idtxtx.getText()));

            int rows = pst.executeUpdate();
            if (rows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Record Updated Successfully!");
            } else {
                showAlert(Alert.AlertType.WARNING, "No record found to update.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error updating: " + e.getMessage());
        }
    }

    // 🗑 DELETE
    @FXML
    void handleDelete(ActionEvent event) {
        if (idtxtx.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Enter User ID to delete.");
            return;
        }

        String sql = "DELETE FROM buyer_interest WHERE id=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, Integer.parseInt(idtxtx.getText()));
            int rows = pst.executeUpdate();
            if (rows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Record Deleted Successfully!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.WARNING, "No record found to delete.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error deleting: " + e.getMessage());
        }
    }

    // CLEAR FORM
    private void clearFields() {
        messageField.clear();
        addressField.clear();
    }

    // ALERT
    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }
}
