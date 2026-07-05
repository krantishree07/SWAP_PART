package com.example.swapmart.securedealcontrol;

import com.example.swapmart.usercontrol.mysqlconnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class securedealcontroller {

    @FXML
    private TextField buyerIdField;
    @FXML
    private TextField buyerNameField;
    @FXML
    private TextField sellerIdField;
    @FXML
    private TextField sellerNameField;
    @FXML
    private TextField productIdField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField conditionField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField amountField;
    @FXML
    private ComboBox<String> dealTypeCombo;
    @FXML
    private TextArea notesField;
    @FXML
    private TextField statusField;

    private Connection con;

    @FXML
    void initialize() {
        try {
            con = mysqlconnection.getConnection();
            dealTypeCombo.getItems().addAll("CASH", "UPI", "EXCHANGE");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error initializing: " + e.getMessage());
        }
    }

    // Buyer ID -> Buyer Name
    @FXML
    void handleBuyerId(ActionEvent event) {
        if (buyerIdField.getText().isEmpty()) return;
        try (PreparedStatement pst = con.prepareStatement("SELECT name FROM users WHERE id=?")) {
            pst.setInt(1, Integer.parseInt(buyerIdField.getText()));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) buyerNameField.setText(rs.getString("name"));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error fetching buyer: " + e.getMessage());
        }
    }

    // Seller ID -> Seller Name
    @FXML
    void handleSellerId(ActionEvent event) {
        if (sellerIdField.getText().isEmpty()) return;
        try (PreparedStatement pst = con.prepareStatement("SELECT name FROM users WHERE id=?")) {
            pst.setInt(1, Integer.parseInt(sellerIdField.getText()));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) sellerNameField.setText(rs.getString("name"));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error fetching seller: " + e.getMessage());
        }
    }

    // Product ID -> Product + Seller Details
    @FXML
    void handleProductId(ActionEvent event) {
        if (productIdField.getText().isEmpty()) return;
        try (PreparedStatement pst = con.prepareStatement(
                "SELECT object_name, category, condition_item, description, price, status, seller_id, seller_name " +
                        "FROM seller WHERE object_id=?")) {
            pst.setInt(1, Integer.parseInt(productIdField.getText()));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                productNameField.setText(rs.getString("object_name"));
                categoryField.setText(rs.getString("category"));
                conditionField.setText(rs.getString("condition_item"));
                descriptionField.setText(rs.getString("description"));
                amountField.setText(rs.getString("price"));
                statusField.setText(rs.getString("status"));

                // Auto-fill Seller details
                sellerIdField.setText(rs.getString("seller_id"));
                sellerNameField.setText(rs.getString("seller_name"));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error fetching product: " + e.getMessage());
        }
    }

    // SECURE DEAL
    // SECURE DEAL
    @FXML
    public void handleSecure(ActionEvent event) {
        if (buyerIdField.getText().isEmpty() ||
                sellerIdField.getText().isEmpty() ||
                productIdField.getText().isEmpty() ||
                amountField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please fill all required fields.");
            return;
        }

        try {
            // Restriction: check product status first
            try (PreparedStatement pstCheck = con.prepareStatement(
                    "SELECT status FROM seller WHERE object_id=?")) {
                pstCheck.setInt(1, Integer.parseInt(productIdField.getText()));
                ResultSet rsCheck = pstCheck.executeQuery();
                if (rsCheck.next()) {
                    String currentStatus = rsCheck.getString("status");
                    if ("UNAVAILABLE".equalsIgnoreCase(currentStatus)) {
                        // 🔥 Explicit message for duplicate purchase attempt
                        showAlert(Alert.AlertType.WARNING,
                                "Product is UNAVAILABLE. You cannot secure this deal again.");
                        return; // stop execution
                    }
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error checking product status: " + e.getMessage());
                return;
            }

            // ✅ Proceed only if product is available
            String sql = "INSERT INTO secure_deals (buyer_id, seller_id, product_id, amount, deal_type, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE amount=?, deal_type=?, notes=?";

            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setInt(1, Integer.parseInt(buyerIdField.getText()));
                pst.setInt(2, Integer.parseInt(sellerIdField.getText()));
                pst.setInt(3, Integer.parseInt(productIdField.getText()));
                pst.setDouble(4, Double.parseDouble(amountField.getText()));
                pst.setString(5, dealTypeCombo.getValue());
                pst.setString(6, notesField.getText());

                pst.setDouble(7, Double.parseDouble(amountField.getText()));
                pst.setString(8, dealTypeCombo.getValue());
                pst.setString(9, notesField.getText());

                int rows = pst.executeUpdate();
                if (rows > 0) {
                    try (PreparedStatement pstSeller = con.prepareStatement(
                            "UPDATE seller SET status='UNAVAILABLE' WHERE object_id=?")) {
                        pstSeller.setInt(1, Integer.parseInt(productIdField.getText()));
                        pstSeller.executeUpdate();
                    }

                    statusField.setText("UNAVAILABLE");
                    showAlert(Alert.AlertType.INFORMATION, "Deal Secured!");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error inserting secure deal: " + e.getMessage());
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error securing deal: " + e.getMessage());
        }
    }


    // DETAIL SNAPSHOT
    @FXML
    void handleDetail(ActionEvent event) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Buyer Name: ").append(buyerNameField.getText()).append(" (ID: ").append(buyerIdField.getText()).append(")\n");
            sb.append("Seller: ").append(sellerNameField.getText()).append(" (ID: ").append(sellerIdField.getText()).append(")\n");
            sb.append("Product: ").append(productNameField.getText()).append(" (ID: ").append(productIdField.getText()).append(")\n");
            sb.append("Category: ").append(categoryField.getText()).append("\n");
            sb.append("Condition: ").append(conditionField.getText()).append("\n");
            sb.append("Description: ").append(descriptionField.getText()).append("\n\n");
            sb.append("Amount: ").append(amountField.getText()).append("\n");
            sb.append("Deal Type: ").append(dealTypeCombo.getValue()).append("\n");
            sb.append("Notes: ").append(notesField.getText()).append("\n");
            sb.append("Status: ").append(statusField.getText()).append("\n");

            showAlert(Alert.AlertType.INFORMATION, sb.toString());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error showing details: " + e.getMessage());
        }
    }

    private void clearFields() {
        try {
            buyerIdField.clear();
            buyerNameField.clear();
            sellerIdField.clear();
            sellerNameField.clear();
            productIdField.clear();
            productNameField.clear();
            categoryField.clear();
            conditionField.clear();
            descriptionField.clear();
            amountField.clear();
            notesField.clear();
            statusField.clear();
            dealTypeCombo.setValue(null);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error clearing fields: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

    public void handleSearch(ActionEvent event) {
        autoFillDetails();
    }

    // AUTO-FILL DETAILS (includes seller info now)
    // AUTO-FILL DETAILS (includes seller info now)
    private void autoFillDetails() {
        try {
            // Buyer Name
            if (!buyerIdField.getText().isEmpty()) {
                try (PreparedStatement pst = con.prepareStatement("SELECT name FROM users WHERE id=?")) {
                    pst.setInt(1, Integer.parseInt(buyerIdField.getText()));
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) buyerNameField.setText(rs.getString("name"));
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error fetching buyer details: " + e.getMessage());
                }
            }

            // Product Details + Seller Info
            if (!productIdField.getText().isEmpty()) {
                try (PreparedStatement pst = con.prepareStatement(
                        "SELECT object_name, category, condition_item, description, price, status, seller_id, seller_name " +
                                "FROM seller WHERE object_id=?")) {
                    pst.setInt(1, Integer.parseInt(productIdField.getText()));
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        productNameField.setText(rs.getString("object_name"));
                        categoryField.setText(rs.getString("category"));
                        conditionField.setText(rs.getString("condition_item"));
                        descriptionField.setText(rs.getString("description"));
                        amountField.setText(rs.getString("price"));
                        statusField.setText(rs.getString("status"));

                        // 🔥 Auto-fill Seller details from product
                        sellerIdField.setText(rs.getString("seller_id"));
                        sellerNameField.setText(rs.getString("seller_name"));
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error fetching product details: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error auto-filling details: " + e.getMessage());
        }
    }
}