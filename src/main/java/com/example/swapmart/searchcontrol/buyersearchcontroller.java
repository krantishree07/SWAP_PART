package com.example.swapmart.searchcontrol;

import com.example.swapmart.usercontrol.mysqlconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class buyersearchcontroller {

    @FXML private ComboBox<String> categoryCombo;
    @FXML private ComboBox<String> objectNameCombo;
    @FXML private TableView<buyersearchbean> tableView;

    @FXML private TableColumn<buyersearchbean, Integer> colObjectId;
    @FXML private TableColumn<buyersearchbean, String> colFrontImage;
    @FXML private TableColumn<buyersearchbean, String> colBackImage;

    // Detail panel
    @FXML private Label lblObjectName;
    @FXML private Label lblCategory;
    @FXML private Label lblCondition;
    @FXML private Label lblDescription;
    @FXML private Label lblExpectedSwap;
    @FXML private Label lblPrice;
    @FXML private Label lblSellerName;
    @FXML private Label lblSellerNumber;
    @FXML private Label lblStatus;
    @FXML private Label lblSellerId;

    private Connection con;

    @FXML
    void initialize() {
        con = mysqlconnection.getConnection();

        // ✅ Bind columns
        colObjectId.setCellValueFactory(d -> d.getValue().objectIdProperty().asObject());
        colFrontImage.setCellValueFactory(d -> d.getValue().frontImagePathProperty());
        colBackImage.setCellValueFactory(d -> d.getValue().backImagePathProperty());

        // ✅ Image rendering with click handler
        setupImageColumn(colFrontImage);
        setupImageColumn(colBackImage);

        // ✅ Load categories
        loadCategories();

        // ✅ Row selection listener
        setupRowSelection();

        // ✅ Category selection listener to load object names
        categoryCombo.setOnAction(e -> loadObjectNames());
    }

    // 🔥 IMAGE COLUMN with click handler
    private void setupImageColumn(TableColumn<buyersearchbean, String> column) {
        column.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(80);
                imageView.setFitWidth(80);
                imageView.setPreserveRatio(true);

                imageView.setOnMouseClicked(event -> {
                    buyersearchbean selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        showDetails(selected);
                    }
                });
            }
            @Override
            protected void updateItem(String path, boolean empty) {
                super.updateItem(path, empty);
                if (empty || path == null || path.isEmpty()) {
                    setGraphic(null);
                } else {
                    File file = new File(path);
                    if (file.exists()) {
                        Image img = new Image(file.toURI().toString());
                        imageView.setImage(img);
                        setGraphic(imageView);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    // 🔥 LOAD CATEGORIES
    private void loadCategories() {
        try {
            categoryCombo.getItems().clear();
            categoryCombo.getItems().add("All");

            PreparedStatement pst = con.prepareStatement("SELECT DISTINCT category FROM seller");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                categoryCombo.getItems().add(rs.getString("category"));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error loading categories: " + e.getMessage());
        }
    }

    // 🔥 LOAD OBJECT NAMES BASED ON CATEGORY
    private void loadObjectNames() {
        try {
            objectNameCombo.getItems().clear();
            String category = categoryCombo.getValue();

            PreparedStatement pst;
            if ("All".equalsIgnoreCase(category)) {
                pst = con.prepareStatement("SELECT DISTINCT object_name FROM seller");
            } else {
                pst = con.prepareStatement("SELECT DISTINCT object_name FROM seller WHERE LOWER(category)=LOWER(?)");
                pst.setString(1, category);
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                objectNameCombo.getItems().add(rs.getString("object_name"));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error loading object names: " + e.getMessage());
        }
    }

    // 🔥 SEARCH BUTTON
    @FXML
    void handleSearch() {
        String category = categoryCombo.getValue();
        String objectName = objectNameCombo.getValue();

        ObservableList<buyersearchbean> list = FXCollections.observableArrayList();

        try {
            String sql;
            PreparedStatement pst;

            if (category == null || "All".equalsIgnoreCase(category)) {
                sql = "SELECT * FROM seller WHERE LOWER(object_name) LIKE LOWER(?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, (objectName == null || objectName.isEmpty()) ? "%" : "%" + objectName + "%");
            } else {
                sql = "SELECT * FROM seller WHERE LOWER(category)=LOWER(?) AND LOWER(object_name) LIKE LOWER(?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, category);
                pst.setString(2, (objectName == null || objectName.isEmpty()) ? "%" : "%" + objectName + "%");
            }

            System.out.println("Executing SQL: " + sql); // 🔍 Debug
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new buyersearchbean(
                        rs.getInt("object_id"),
                        rs.getString("object_name"),
                        rs.getString("category"),
                        rs.getString("condition_item"),
                        rs.getString("description"),
                        rs.getString("expected_swap"),
                        rs.getDouble("price"),
                        rs.getString("seller_id"),
                        rs.getString("seller_name"),
                        rs.getString("seller_number"),
                        rs.getString("front_image_path"),
                        rs.getString("back_image_path"),
                        rs.getString("status")
                ));
            }

            tableView.setItems(list);

            if (list.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No results found.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Search error: " + e.getMessage());
        }
    }

    // 🔥 ROW SELECT
    private void setupRowSelection() {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showDetails(newVal);
            }
        });
    }

    // 🔥 DETAILS PANEL
    private void showDetails(buyersearchbean b) {
        lblObjectName.setText("Name: " + b.getObjectName());
        lblCategory.setText("Category: " + b.getCategory());
        lblCondition.setText("Condition: " + b.getConditionItem());
        lblDescription.setText("Description: " + b.getDescription());
        lblExpectedSwap.setText("Expected: " + b.getExpectedSwap());
        lblPrice.setText("Price: " + b.getPrice());
        lblSellerName.setText("Seller: " + b.getSellerName());
        lblSellerNumber.setText("Mobile: " + b.getSellerNumber());
        lblSellerId.setText("Seller ID: " + b.getSellerId());
        lblStatus.setText("Status: " + b.getStatus());
    }

    // 🔥 ALERT
    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

    public void handleSecure(ActionEvent event) { }
    public void handleUpdate(ActionEvent event) { }
    public void handleDelete(ActionEvent event) { }
}
