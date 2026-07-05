package com.example.swapmart.usercontrol;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class usercontroller {

    // TextFields from FXML
    @FXML private TextField addresstxt;
    @FXML private TextField emailtxt;
    @FXML private TextField idtxt;
    @FXML private TextField nametxt;
    @FXML private TextField phonetxt;

    // Role selection combobox
    @FXML private ComboBox<String> selectroletxt;

    // Photo upload controls
    @FXML private Button uploadButton;
    @FXML private ImageView uploadedImage;

    // Database connection object
    private Connection conn;

    // Selected file for photo
    private File selectedFile;

    // Method to show popup message
    void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SwapMart");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }

    // Method to check if all fields are filled
    boolean validateFields() {
        if (nametxt.getText().isEmpty() ||
                emailtxt.getText().isEmpty() ||
                phonetxt.getText().isEmpty() ||
                addresstxt.getText().isEmpty() ||
                selectroletxt.getValue() == null) {
            showMessage("Please fill all information before saving.");
            return false;
        }
        return true;
    }

    // Method to auto generate next available ID
    void generateID() {
        try {
            conn = mysqlconnection.getConnection();
            String sql = "SELECT IFNULL(MIN(t1.id + 1),1) as nextID " +
                    "FROM users t1 LEFT JOIN users t2 ON t1.id + 1 = t2.id " +
                    "WHERE t2.id IS NULL";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                idtxt.setText(rs.getString("nextID"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Clear all fields
    @FXML
    void doclear(ActionEvent event) {
        nametxt.clear();
        emailtxt.clear();
        phonetxt.clear();
        addresstxt.clear();
        selectroletxt.setValue(null);
        uploadedImage.setImage(null);
        selectedFile = null;
        generateID();
    }

    // Save data into database
    @FXML
    void dosave(ActionEvent event) {
        if (!validateFields()) return;

        try {
            conn = mysqlconnection.getConnection();
            String sql = "INSERT INTO users (id, name, email, phone, password, role, photo) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, idtxt.getText());
            pst.setString(2, nametxt.getText());
            pst.setString(3, emailtxt.getText());
            pst.setString(4, phonetxt.getText());
            pst.setString(5, addresstxt.getText());
            pst.setString(6, selectroletxt.getValue());

            if (selectedFile != null) {
                FileInputStream fis = new FileInputStream(selectedFile);
                pst.setBinaryStream(7, fis, (int) selectedFile.length());
            } else {
                pst.setNull(7, java.sql.Types.BLOB);
            }

            pst.executeUpdate();
            showMessage("User saved successfully! with ID " + idtxt.getText());
            doclear(null);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // Update user information
    @FXML
    void doupdate(ActionEvent event) {
        if (!validateFields()) return;

        try {
            conn = mysqlconnection.getConnection();
            String sql = "UPDATE users SET name=?, email=?, phone=?, password=?, role=?, photo=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, nametxt.getText());
            pst.setString(2, emailtxt.getText());
            pst.setString(3, phonetxt.getText());
            pst.setString(4, addresstxt.getText());
            pst.setString(5, selectroletxt.getValue());

            if (selectedFile != null) {
                FileInputStream fis = new FileInputStream(selectedFile);
                pst.setBinaryStream(6, fis, (int) selectedFile.length());
            } else {
                pst.setNull(6, java.sql.Types.BLOB);
            }

            pst.setString(7, idtxt.getText());
            pst.executeUpdate();
            showMessage("User updated successfully!");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // Search user by ID
    @FXML
    void dosearch(ActionEvent event) {
        try {
            if (idtxt.getText().isEmpty()) {
                showMessage("Please enter User ID to search!");
                return;
            }

            conn = mysqlconnection.getConnection();
            String sql = "SELECT * FROM users WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idtxt.getText());
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                nametxt.setText(rs.getString("name"));
                emailtxt.setText(rs.getString("email"));
                phonetxt.setText(rs.getString("phone"));
                addresstxt.setText(rs.getString("password"));
                selectroletxt.setValue(rs.getString("role"));

                byte[] imgBytes = rs.getBytes("photo");
                if (imgBytes != null) {
                    uploadedImage.setImage(new Image(new ByteArrayInputStream(imgBytes)));
                } else {
                    uploadedImage.setImage(null);
                }

                showMessage("User Found");
            } else {
                showMessage("User not found");
                doclear(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error while searching!");
        }
    }

    // Delete user
    @FXML
    void dodelete(ActionEvent event) {
        try {
            conn = mysqlconnection.getConnection();
            String sql = "DELETE FROM users WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idtxt.getText());
            pst.executeUpdate();
            showMessage("User deleted successfully!");
            generateID();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // Initialize method runs automatically when form loads
    @FXML
    void initialize() {
        selectroletxt.getItems().addAll("Buyer", "Seller", "Buyer and Seller");
        generateID();
        uploadButton.setOnAction(e -> handleUploadPhoto());
    }

    // Handle photo upload
    private void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            uploadedImage.setImage(image);
        }
    }
}
