package com.example.swapmart.useranalysiscontrol;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.swapmart.usercontrol.mysqlconnection;

public class useranalysiscontroller {

    @FXML
    private PieChart pieChart; // Role Distribution

    @FXML
    private BarChart<String, Number> barChart; // Product Categories

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    // Chart for Sales by Day of Week
    @FXML
    private BarChart<String, Number> salesBarChart;

    Connection con;

    @FXML
    void initialize() {
        con = mysqlconnection.getConnection();

        loadRoleDistribution();
        loadCategoryDistribution();
        loadSalesByDay();
    }

    // 🔥 PieChart for Role Distribution
    private void loadRoleDistribution() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        try {
            String sql = "SELECT role, COUNT(*) as count FROM users GROUP BY role";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                pieData.add(new PieChart.Data(rs.getString("role"), rs.getInt("count")));
            }

            pieChart.setData(pieData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 BarChart for Product Categories
    private void loadCategoryDistribution() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Products by Category");

        try {
            String sql = "SELECT category, COUNT(*) as count FROM seller GROUP BY category";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("category"), rs.getInt("count")));
            }

            barChart.getData().clear();
            barChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 BarChart for Sales by Day of Week
    private void loadSalesByDay() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales Count");

        try {
            // Group by weekday and order Monday → Sunday
            String sql = "SELECT DAYNAME(deal_date) AS day, COUNT(*) AS count " +
                    "FROM secure_deals " +
                    "GROUP BY day " +
                    "ORDER BY FIELD(day,'Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday')";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String weekday = rs.getString("day");
                int count = rs.getInt("count");
                series.getData().add(new XYChart.Data<>(weekday, count));
            }

            salesBarChart.getData().clear();
            salesBarChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
