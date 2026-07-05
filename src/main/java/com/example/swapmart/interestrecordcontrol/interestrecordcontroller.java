package com.example.swapmart.interestrecordcontrol;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.*;
import java.io.FileWriter;

import static com.example.swapmart.usercontrol.mysqlconnection.getConnection;

public class interestrecordcontroller {

    @FXML private TableView<interestrecordbean> interestTable;
    @FXML private TableColumn<interestrecordbean, Integer> idCol;
    @FXML private TableColumn<interestrecordbean, String> nameCol;
    @FXML private TableColumn<interestrecordbean, String> phoneCol;
    @FXML private TableColumn<interestrecordbean, String> messageCol;
    @FXML private TableColumn<interestrecordbean, String> addressCol;
    @FXML private TableColumn<interestrecordbean, Timestamp> createdAtCol;
    @FXML private Button exportToExcel;


    private ObservableList<interestrecordbean> data = FXCollections.observableArrayList();
  Connection conn;
    @FXML
    public void initialize() {
        // Bind table columns
        idCol.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        nameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());
        phoneCol.setCellValueFactory(cell -> cell.getValue().phoneProperty());
        messageCol.setCellValueFactory(cell -> cell.getValue().messageProperty());
        addressCol.setCellValueFactory(cell -> cell.getValue().addressProperty());
        createdAtCol.setCellValueFactory(cell -> cell.getValue().createdAtProperty());

        // Load all records immediately
        loadAllRecords();

        // Export button action
        exportToExcel.setOnAction(e -> exportToExcel());
        conn = getConnection();
    }


    private void loadAllRecords() {
        data.clear();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM buyer_interest")) {

            while (rs.next()) {
                data.add(new interestrecordbean(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("message"),
                        rs.getString("address"),
                        rs.getTimestamp("created_at")
                ));
            }
            interestTable.setItems(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void exportToExcel() {
        try {
            if (interestTable.getItems().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Export Warning");
                alert.setHeaderText(null);
                alert.setContentText("No data to export!");
                alert.showAndWait();
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );

            File selectedFile = fileChooser.showSaveDialog(exportToExcel.getScene().getWindow());
            if (selectedFile == null) {
                System.out.println("Export cancelled.");
                return;
            }

            // Ensure .csv extension
            if (!selectedFile.getName().toLowerCase().endsWith(".csv")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".csv");
            }

            try (FileWriter writer = new FileWriter(selectedFile)) {
                // Header row
                writer.write("ID,Name,Phone,Message,Address,CreatedAt\n");

                ObservableList<interestrecordbean> data = interestTable.getItems();
                for (interestrecordbean bean : data) {
                    writer.write(
                            "\"" + bean.getId() + "\"," +
                                    "\"" + bean.getName() + "\"," +
                                    "\"" + bean.getPhone() + "\"," +
                                    "\"" + bean.getMessage() + "\"," +
                                    "\"" + bean.getAddress() + "\"," +
                                    "\"" + bean.getCreatedAt() + "\"\n"
                    );
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Successful");
            alert.setHeaderText(null);
            alert.setContentText("Data exported to " + selectedFile.getAbsolutePath());
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Export Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to export: " + e.getMessage());
            alert.showAndWait();
        }
    }

}
