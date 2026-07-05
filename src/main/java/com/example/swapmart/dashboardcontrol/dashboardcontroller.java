package com.example.swapmart.dashboardcontrol;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import com.example.swapmart.HelloApplication;
import com.example.swapmart.usercontrol.mysqlconnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class dashboardcontroller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    Connection con;

    // 🔥 Generic method to load scenes
    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 1 USER
    @FXML
    void user(ActionEvent event) {
        loadScene("userxml/userview.fxml", "User");
    }

    // 2 SELLER
    @FXML
    void seller(ActionEvent event) {
        loadScene("sellerxml/sellerview.fxml", "Seller");
    }

    // 3 SEARCH
    @FXML
    void search(ActionEvent event) {
        loadScene("searchcontrolxml/searchcontrolview.fxml", "Search");
    }

    // 4 INTEREST
    @FXML
    void buyerInterest(ActionEvent event) {
        loadScene("buyerinterestxml/buyerinterestxml.fxml", "Buyer Interest");
    }

    // 5 DEAL
    @FXML
    void secureDeal(ActionEvent event) {
        loadScene("securedealxml/securedealview.fxml", "Secure Deal");
    }

    // 6 USER RECORD
    @FXML
    void userRecord(ActionEvent event) {
        loadScene("userrecordxml/userrecordview.fxml", "User Records");
    }

    // 7 PRODUCT RECORD
    @FXML
    void productRecord(ActionEvent event) {
        loadScene("productrecordxml/productrecordcontrolview.fxml", "Product Records");
    }

    // 8 ANALYSIS
    @FXML
    void userAnalysis(ActionEvent event) {
        loadScene("useranalysiscontrolxml/useranalysiscontrolview.fxml", "User & Product Analysis");
    }

    // 9 DEAL RECORD
    @FXML
    void dealRecord(ActionEvent event) {
        loadScene("dealrecordxml/dealrecordview.fxml", "Deal Records");
    }

    // 10 REPORTS (placeholder)
    @FXML
    void reports(ActionEvent event) {
        loadScene("interestrecordxml/interestrecordview.fxml", "Reports");
    }

    @FXML
    void initialize() {
        con = mysqlconnection.getConnection();
        if (con == null) {
            System.out.println("Connection error.");
        }
    }
}
