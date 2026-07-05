package com.example.swapmart.productrecordcontrol;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class productrecordbean {
    private int id;
    private String name;
    private String category;
    private String condition;
    private double price;
    private String sellerId;
    private String sellerName;
    private String status;

    private ImageView frontPhoto;
    private ImageView backPhoto;

    // Constructor
    public productrecordbean(int id, String name, String category, String condition,
                             double price, String sellerId, String sellerName,
                             String status, String frontImagePath, String backImagePath) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.condition = condition;
        this.price = price;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.status = status;

        // Convert image paths to thumbnails
        if (frontImagePath != null && !frontImagePath.isEmpty()) {
            this.frontPhoto = new ImageView(new Image("file:" + frontImagePath, 80, 80, true, true));
        } else {
            this.frontPhoto = new ImageView(); // empty placeholder
        }

        if (backImagePath != null && !backImagePath.isEmpty()) {
            this.backPhoto = new ImageView(new Image("file:" + backImagePath, 80, 80, true, true));
        } else {
            this.backPhoto = new ImageView(); // empty placeholder
        }
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getCondition() { return condition; }
    public double getPrice() { return price; }
    public String getSellerId() { return sellerId; }
    public String getSellerName() { return sellerName; }
    public String getStatus() { return status; }
    public ImageView getFrontPhoto() { return frontPhoto; }
    public ImageView getBackPhoto() { return backPhoto; }

    // Setters (optional if you want mutability)
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setPrice(double price) { this.price = price; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
    public void setStatus(String status) { this.status = status; }
    public void setFrontPhoto(ImageView frontPhoto) { this.frontPhoto = frontPhoto; }
    public void setBackPhoto(ImageView backPhoto) { this.backPhoto = backPhoto; }
}
