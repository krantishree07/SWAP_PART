package com.example.swapmart.sellercontrol;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.swapmart.usercontrol.mysqlconnection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.swing.JOptionPane;

public class sellercontroller {

    @FXML
    private ComboBox<String> categorycombo;
    @FXML
    private ComboBox<String> conditioncombo;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField expectedswaptxt;
    @FXML
    private TextField objectidtxt;
    @FXML
    private TextField objectnametxt;
    @FXML
    private TextField pricetxt;
    @FXML
    private TextField selleridtxt;
    @FXML
    private TextField sellernametxt;
    @FXML
    private TextField sellernumbertxt;
    @FXML
    private ImageView frontPhotoView;
    @FXML
    private ImageView backPhotoView;

    Connection con;
    String frontPicPath;
    String backPicPath;

    /* Disable object fields */
    void disableObjectFields(boolean status) {
        objectidtxt.setDisable(status);
        objectnametxt.setDisable(status);
        categorycombo.setDisable(status);
        conditioncombo.setDisable(status);
        descriptionArea.setDisable(status);
        expectedswaptxt.setDisable(status);
        pricetxt.setDisable(status);
    }

    /* Auto generate object ID */
    void generateObjectId() {
        try {
            PreparedStatement pst = con.prepareStatement("select max(object_id) from seller");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                objectidtxt.setText(String.valueOf(id + 1));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Search seller when ID typed */
    @FXML
    void searchSellerById() {
        try {
            String id = selleridtxt.getText();
            if (id.isEmpty()) return;

            String sql = "select name,phone from users where id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                sellernametxt.setText(rs.getString("name"));
                sellernumbertxt.setText(rs.getString("phone"));
                disableObjectFields(false);
                generateObjectId();
            } else {
                sellernametxt.clear();
                sellernumbertxt.clear();
                disableObjectFields(true);
                JOptionPane.showMessageDialog(null, "User not registered. Please register first.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Add object */
    @FXML
    void doaddobject(ActionEvent event) {
        try {
            if (objectnametxt.getText().isEmpty() ||
                    categorycombo.getValue() == null ||
                    conditioncombo.getValue() == null ||
                    pricetxt.getText().isEmpty() ||
                    selleridtxt.getText().isEmpty() ||
                    sellernametxt.getText().isEmpty() ||
                    sellernumbertxt.getText().isEmpty()) {

                JOptionPane.showMessageDialog(null, "Please fill all required fields!");
                return;
            }

            String sql = "insert into seller(object_id,object_name,category,condition_item,description,expected_swap,price,seller_id,seller_name,seller_number,front_image_path,back_image_path) values(?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, objectidtxt.getText());
            pst.setString(2, objectnametxt.getText());
            pst.setString(3, categorycombo.getValue());
            pst.setString(4, conditioncombo.getValue());
            pst.setString(5, descriptionArea.getText());
            pst.setString(6, expectedswaptxt.getText());
            pst.setString(7, pricetxt.getText());
            pst.setString(8, selleridtxt.getText());
            pst.setString(9, sellernametxt.getText());
            pst.setString(10, sellernumbertxt.getText());
            pst.setString(11, frontPicPath);
            pst.setString(12, backPicPath);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Object Added Successfully!");
            generateObjectId();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Clear form */
    @FXML
    void doclearobject(ActionEvent event) {
        selleridtxt.clear();
        sellernametxt.clear();
        sellernumbertxt.clear();
        objectidtxt.clear();
        objectnametxt.clear();
        categorycombo.setValue(null);
        conditioncombo.setValue(null);
        pricetxt.clear();
        descriptionArea.clear();
        expectedswaptxt.clear();
        frontPhotoView.setImage(null);
        backPhotoView.setImage(null);
        disableObjectFields(true);
    }

    /* Delete object */
    @FXML
    void doremoveobject(ActionEvent event) {
        try {
            if (objectidtxt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Enter Object ID first!");
                return;
            }

            String sql = "DELETE FROM seller WHERE object_id=? AND seller_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, objectidtxt.getText());
            pst.setString(2, selleridtxt.getText());

            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Object Deleted Successfully");
                doclearobject(null);
            } else {
                JOptionPane.showMessageDialog(null, "Object ID not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Search object */
    @FXML
    void dosearchobject(ActionEvent event) {
        try {
            String sql = "select * from seller where object_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, objectidtxt.getText());
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                objectnametxt.setText(rs.getString("object_name"));
                categorycombo.setValue(rs.getString("category"));
                conditioncombo.setValue(rs.getString("condition_item"));
                descriptionArea.setText(rs.getString("description"));
                expectedswaptxt.setText(rs.getString("expected_swap"));
                pricetxt.setText(rs.getString("price"));

                selleridtxt.setText(rs.getString("seller_id"));
                sellernametxt.setText(rs.getString("seller_name"));
                sellernumbertxt.setText(rs.getString("seller_number"));

                frontPicPath = rs.getString("front_image_path");
                backPicPath = rs.getString("back_image_path");

                if (frontPicPath != null && !frontPicPath.isEmpty()) {
                    Image img = new Image(new File(frontPicPath).toURI().toString());
                    frontPhotoView.setImage(img);
                }
                if (backPicPath != null && !backPicPath.isEmpty()) {
                    Image img = new Image(new File(backPicPath).toURI().toString());
                    backPhotoView.setImage(img);
                }

                JOptionPane.showMessageDialog(null, "Object Found");
            } else {
                JOptionPane.showMessageDialog(null, "Object Not Found");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Update object */
    @FXML
    public void doupdateobject(ActionEvent event) {
        try {
            String sql = "update seller set object_name=?, category=?, condition_item=?, description=?, expected_swap=?, price=?, seller_id=?, seller_name=?, seller_number=?, front_image_path=?, back_image_path=? where object_id=?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, objectnametxt.getText());
            pst.setString(2, categorycombo.getValue());
            pst.setString(3, conditioncombo.getValue());
            pst.setString(4, descriptionArea.getText());
            pst.setString(5, expectedswaptxt.getText());
            pst.setString(6, pricetxt.getText());
            pst.setString(7, selleridtxt.getText());
            pst.setString(8, sellernametxt.getText());
            pst.setString(9, sellernumbertxt.getText());
            pst.setString(10, frontPicPath);
            pst.setString(11, backPicPath);
            pst.setString(12, objectidtxt.getText());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Object Updated Successfully");
            } else {
                JOptionPane.showMessageDialog(null, "Object Not Found");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Upload front photo */
    @FXML
    public void douploadfrontphoto(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png")
        );

        File file = chooser.showOpenDialog(null);
        if (file != null) {
            frontPicPath = file.getAbsolutePath();   // ✅ store path
            try {
                Image img = new Image(new FileInputStream(file));
                frontPhotoView.setImage(img);        // ✅ show preview
            } catch (Exception e) {
                System.out.println("Error loading front photo: " + e.getMessage());
            }
        }
    }

    @FXML
    public void douploadbackphoto(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png")
        );

        File file = chooser.showOpenDialog(null);
        if (file != null) {
            backPicPath = file.getAbsolutePath();   // ✅ store path
            try {
                Image img = new Image(new FileInputStream(file));
                backPhotoView.setImage(img);        // ✅ show preview
            } catch (Exception e) {
                System.out.println("Error loading front photo: " + e.getMessage());
            }
        }
    }

    @FXML
    void initialize() {
        try {
            con = mysqlconnection.getConnection();
            if (con == null) {
                System.out.println("Database connection failed!");
            } else {
                System.out.println("Network established");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        categorycombo.getItems().addAll("Electronics", "Books", "Clothes", "Furniture", "Home Appliances", "Sports & Fitness", "Toys & Games", "Vehicles");
        conditioncombo.getItems().addAll("New", "Like New", "Good", "Fair", "Used", "Refurbished");
    }

}
