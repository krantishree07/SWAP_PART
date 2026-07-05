package com.example.swapmart.productrecordcontrol;

import com.example.swapmart.usercontrol.mysqlconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class productrecordcontroller {

    @FXML private TableView<productrecordbean> productTable;
    @FXML private TableColumn<productrecordbean, Integer> colId;
    @FXML private TableColumn<productrecordbean, String> colName;
    @FXML private TableColumn<productrecordbean, String> colCategory;
    @FXML private TableColumn<productrecordbean, String> colCondition;
    @FXML private TableColumn<productrecordbean, Double> colPrice;
    @FXML private TableColumn<productrecordbean, String> colSellerId;
    @FXML private TableColumn<productrecordbean, String> colSellerName;
    @FXML private TableColumn<productrecordbean, String> colStatus;

    // 🔥 New photo columns
    @FXML private TableColumn<productrecordbean, ImageView> colFrontPhoto;
    @FXML private TableColumn<productrecordbean, ImageView> colBackPhoto;

    @FXML private ComboBox<String> categoryCombo;
    @FXML private Button searchBtn;
    @FXML private Button exportXmlBtn;

    private Connection con;
    private ObservableList<productrecordbean> productList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        con = mysqlconnection.getConnection();

        // Bind columns
        colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("category"));
        colCondition.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("condition"));
        colPrice.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("price"));
        colSellerId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("sellerId"));
        colSellerName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("sellerName"));
        colStatus.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("status"));

        // 🔥 Bind photo columns
        colFrontPhoto.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("frontPhoto"));
        colBackPhoto.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("backPhoto"));

        loadCategories();
        loadProducts("All");

        // Button actions
        searchBtn.setOnAction(e -> loadProducts(categoryCombo.getValue()));
        exportXmlBtn.setOnAction(e -> exportToXML());
    }

    // Load distinct categories into ComboBox
    private void loadCategories() {
        categoryCombo.getItems().clear();
        categoryCombo.getItems().add("All");

        try {
            String sql = "SELECT DISTINCT category FROM seller";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                categoryCombo.getItems().add(rs.getString("category"));
            }
            categoryCombo.setValue("All");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load products based on category
    private void loadProducts(String category) {
        productList.clear();
        try {
            String sql = category.equals("All") ?
                    "SELECT * FROM seller" :
                    "SELECT * FROM seller WHERE category=?";
            PreparedStatement pst = con.prepareStatement(sql);
            if (!category.equals("All")) {
                pst.setString(1, category);
            }
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                productList.add(new productrecordbean(
                        rs.getInt("object_id"),
                        rs.getString("object_name"),
                        rs.getString("category"),
                        rs.getString("condition_item"),
                        rs.getDouble("price"),
                        rs.getString("seller_id"),
                        rs.getString("seller_name"),
                        rs.getString("status"),
                        rs.getString("front_image_path"),   // 🔥 Added
                        rs.getString("back_image_path")     // 🔥 Added
                ));
            }
            productTable.setItems(productList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Export to XML
    private void exportToXML() {
        try {
            if (productTable.getItems().isEmpty()) {
                System.out.println("No data to export!");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save XML File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("XML Files", "*.xml")
            );
            File selectedFile = fileChooser.showSaveDialog(null);

            if (selectedFile == null) return;
            if (!selectedFile.getName().toLowerCase().endsWith(".xml")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".xml");
            }

            try (FileWriter writer = new FileWriter(selectedFile)) {
                writer.write("<Products>\n");
                for (productrecordbean bean : productTable.getItems()) {
                    writer.write("  <Product>\n");
                    writer.write("    <ID>" + bean.getId() + "</ID>\n");
                    writer.write("    <Name>" + bean.getName() + "</Name>\n");
                    writer.write("    <Category>" + bean.getCategory() + "</Category>\n");
                    writer.write("    <Condition>" + bean.getCondition() + "</Condition>\n");
                    writer.write("    <Price>" + bean.getPrice() + "</Price>\n");
                    writer.write("    <SellerID>" + bean.getSellerId() + "</SellerID>\n");
                    writer.write("    <SellerName>" + bean.getSellerName() + "</SellerName>\n");
                    writer.write("    <Status>" + bean.getStatus() + "</Status>\n");
                    // 🔥 Export image paths too
                    writer.write("    <FrontPhoto>" + (bean.getFrontPhoto().getImage() != null ? bean.getFrontPhoto().getImage().getUrl() : "") + "</FrontPhoto>\n");
                    writer.write("    <BackPhoto>" + (bean.getBackPhoto().getImage() != null ? bean.getBackPhoto().getImage().getUrl() : "") + "</BackPhoto>\n");
                    writer.write("  </Product>\n");
                }
                writer.write("</Products>");
            }
            System.out.println("Export Successful: " + selectedFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
