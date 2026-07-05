package com.example.swapmart.dealrecordcontrol;

import com.example.swapmart.usercontrol.mysqlconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class dealrecordcontroller {

    @FXML private ComboBox<String> buyerIdCombo;
    @FXML private TableView<dealrecordbean> dealTable;
    @FXML private TableColumn<dealrecordbean, Integer> colDealId;
    @FXML private TableColumn<dealrecordbean, Integer> colBuyerId;
    @FXML private TableColumn<dealrecordbean, Integer> colSellerId;
    @FXML private TableColumn<dealrecordbean, Integer> colProductId;
    @FXML private TableColumn<dealrecordbean, Double> colAmount;
    @FXML private TableColumn<dealrecordbean, String> colDealType;
    @FXML private TableColumn<dealrecordbean, String> colNotes;

    private Connection con;

    @FXML
    void initialize() {
        try {
            con = mysqlconnection.getConnection();
            loadBuyerIds();

            // Bind columns
            colDealId.setCellValueFactory(d -> d.getValue().dealIdProperty().asObject());
            colBuyerId.setCellValueFactory(d -> d.getValue().buyerIdProperty().asObject());
            colSellerId.setCellValueFactory(d -> d.getValue().sellerIdProperty().asObject());
            colProductId.setCellValueFactory(d -> d.getValue().productIdProperty().asObject());
            colAmount.setCellValueFactory(d -> d.getValue().amountProperty().asObject());
            colDealType.setCellValueFactory(d -> d.getValue().dealTypeProperty());
            colNotes.setCellValueFactory(d -> d.getValue().notesProperty());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Initialization error: " + e.getMessage());
        }
    }

    // Load Buyer IDs into ComboBox
    private void loadBuyerIds() {
        try (PreparedStatement pst = con.prepareStatement("SELECT DISTINCT buyer_id FROM secure_deals")) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                buyerIdCombo.getItems().add(rs.getString("buyer_id"));
            }
            // 🔥 Add "All" option
            buyerIdCombo.getItems().add("All");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error loading buyer IDs: " + e.getMessage());
        }
    }

    // Search deals by buyer ID (with All option)
    @FXML
    void handleSearch(ActionEvent event) {
        String buyerId = buyerIdCombo.getValue();
        ObservableList<dealrecordbean> list = FXCollections.observableArrayList();

        try {
            String sql;
            PreparedStatement pst;

            if ("All".equalsIgnoreCase(buyerId)) {
                sql = "SELECT * FROM secure_deals";
                pst = con.prepareStatement(sql);
            } else {
                sql = "SELECT * FROM secure_deals WHERE buyer_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, buyerId);
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new dealrecordbean(
                        rs.getInt("id"),
                        rs.getInt("buyer_id"),
                        rs.getInt("seller_id"),
                        rs.getInt("product_id"),
                        rs.getDouble("amount"),
                        rs.getString("deal_type"),
                        rs.getString("notes")
                ));
            }
            dealTable.setItems(list);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Search error: " + e.getMessage());
        }
    }

    // Export to Excel (CSV + open in File Explorer)
    @FXML
    void handleExport(ActionEvent event) {
        try {
            StringBuilder sb = new StringBuilder();
            for (dealrecordbean d : dealTable.getItems()) {
                sb.append(d.getDealId()).append(",")
                        .append(d.getBuyerId()).append(",")
                        .append(d.getSellerId()).append(",")
                        .append(d.getProductId()).append(",")
                        .append(d.getAmount()).append(",")
                        .append(d.getDealType()).append(",")
                        .append(d.getNotes()).append("\n");
            }

            java.nio.file.Path filePath = java.nio.file.Paths.get("deal_records.csv");
            java.nio.file.Files.write(filePath, sb.toString().getBytes());

            showAlert(Alert.AlertType.INFORMATION, "Exported to deal_records.csv successfully!");

            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            desktop.open(filePath.toFile());

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Export error: " + e.getMessage());
        }
    }

    // Alert utility
    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }
}
