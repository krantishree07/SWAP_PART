package com.example.swapmart.userrecordcontrol;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.example.swapmart.usercontrol.mysqlconnection;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class userrecordcontroller {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TableColumn<userrecordbean, Integer> colId;
    @FXML private TableColumn<userrecordbean, String> colName;
    @FXML private TableColumn<userrecordbean, String> colEmail;
    @FXML private TableColumn<userrecordbean, String> colPhone;
    @FXML private TableColumn<userrecordbean, String> colAddress;
    @FXML private TableColumn<userrecordbean, String> colRole;
    @FXML private TableColumn<userrecordbean, ImageView> colPhoto; // NEW column for photo

    @FXML private TableView<userrecordbean> tableView;
    @FXML private ComboBox<String> userTypeCombo;

    Connection con;
    ObservableList<userrecordbean> userList = FXCollections.observableArrayList();

    // 🔥 MODEL CLASS
    public static class userrecordbean {
        private int id;
        private String name, email, phone, address, role;
        private byte[] photo;

        public userrecordbean(int id, String name, String email, String phone, String address, String role, byte[] photo) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.address = address;
            this.role = role;
            this.photo = photo;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public String getRole() { return role; }
        public byte[] getPhoto() { return photo; }
    }

    @FXML
    void initialize() {
        con = mysqlconnection.getConnection();

        // Bind columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Photo column binding
        colPhoto.setCellValueFactory(data -> {
            byte[] imgBytes = data.getValue().getPhoto();
            if (imgBytes != null) {
                ImageView imgView = new ImageView(new Image(new ByteArrayInputStream(imgBytes)));
                imgView.setFitHeight(50);
                imgView.setFitWidth(50);
                imgView.setPreserveRatio(true);
                return new SimpleObjectProperty<>(imgView);
            } else {
                return new SimpleObjectProperty<>(null);
            }
        });

        // Combo values
        userTypeCombo.getItems().addAll("All", "Buyer", "Seller", "Buyer and Seller");
        userTypeCombo.setValue("All");

        loadData();
    }

    // 🔥 LOAD DATA FROM DB
    void loadData() {
        userList.clear();
        try {
            String sql = "SELECT * FROM users";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                userList.add(new userrecordbean(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password"),   // currently used for address
                        rs.getString("role"),
                        rs.getBytes("photo")        // NEW photo column
                ));
            }
            tableView.setItems(userList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 FILTER BUTTON
    @FXML
    void showRecords(ActionEvent event) {
        String selectedRole = userTypeCombo.getValue();
        ObservableList<userrecordbean> filteredList = FXCollections.observableArrayList();

        for (userrecordbean u : userList) {
            if (selectedRole.equals("All") || u.getRole().equalsIgnoreCase(selectedRole)) {
                filteredList.add(u);
            }
        }
        tableView.setItems(filteredList);
    }

    // 🔥 EXPORT (simple version)
    @FXML
    void exportToExcel(ActionEvent event) {
        try {
            if (tableView.getItems().isEmpty()) {
                System.out.println("No data to export!");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );

            File selectedFile = fileChooser.showSaveDialog(null);
            if (selectedFile == null) {
                System.out.println("Export cancelled.");
                return;
            }

            if (!selectedFile.getName().toLowerCase().endsWith(".csv")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".csv");
            }

            try (FileWriter writer = new FileWriter(selectedFile)) {
                writer.write("ID,Name,Email,Phone,Address,Role\n");
                ObservableList<userrecordbean> data = tableView.getItems();

                for (userrecordbean bean : data) {
                    writer.write(
                            "\"" + bean.getId() + "\"," +
                                    "\"" + bean.getName() + "\"," +
                                    "\"" + bean.getEmail() + "\"," +
                                    "\"" + bean.getPhone() + "\"," +
                                    "\"" + bean.getAddress() + "\"," +
                                    "\"" + bean.getRole() + "\"\n"
                    );
                }
            }
            System.out.println("Export Success!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
