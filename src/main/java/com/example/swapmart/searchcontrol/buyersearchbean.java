package com.example.swapmart.searchcontrol;

import javafx.beans.property.*;

public class buyersearchbean {
    private final IntegerProperty objectId;
    private final StringProperty objectName;
    private final StringProperty category;
    private final StringProperty conditionItem;
    private final StringProperty description;
    private final StringProperty expectedSwap;
    private final DoubleProperty price;
    private final StringProperty sellerId;
    private final StringProperty sellerName;
    private final StringProperty sellerNumber;
    private final StringProperty frontImagePath;
    private final StringProperty backImagePath;
    private final StringProperty status;

    // ✅ Constructor
    public buyersearchbean(int objectId, String objectName, String category,
                           String conditionItem, String description, String expectedSwap,
                           double price, String sellerId, String sellerName, String sellerNumber,
                           String frontImagePath, String backImagePath, String status) {
        this.objectId = new SimpleIntegerProperty(objectId);
        this.objectName = new SimpleStringProperty(objectName);
        this.category = new SimpleStringProperty(category);
        this.conditionItem = new SimpleStringProperty(conditionItem);
        this.description = new SimpleStringProperty(description);
        this.expectedSwap = new SimpleStringProperty(expectedSwap);
        this.price = new SimpleDoubleProperty(price);
        this.sellerId = new SimpleStringProperty(sellerId);
        this.sellerName = new SimpleStringProperty(sellerName);
        this.sellerNumber = new SimpleStringProperty(sellerNumber);
        this.frontImagePath = new SimpleStringProperty(frontImagePath);
        this.backImagePath = new SimpleStringProperty(backImagePath);
        this.status = new SimpleStringProperty(status);
    }

    // ✅ Properties for TableView binding
    public IntegerProperty objectIdProperty() { return objectId; }
    public StringProperty objectNameProperty() { return objectName; }
    public StringProperty categoryProperty() { return category; }
    public StringProperty conditionItemProperty() { return conditionItem; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty expectedSwapProperty() { return expectedSwap; }
    public DoubleProperty priceProperty() { return price; }
    public StringProperty sellerIdProperty() { return sellerId; }
    public StringProperty sellerNameProperty() { return sellerName; }
    public StringProperty sellerNumberProperty() { return sellerNumber; }
    public StringProperty frontImagePathProperty() { return frontImagePath; }
    public StringProperty backImagePathProperty() { return backImagePath; }
    public StringProperty statusProperty() { return status; }

    // ✅ Getters for detail panel
    public int getObjectId() { return objectId.get(); }
    public String getObjectName() { return objectName.get(); }
    public String getCategory() { return category.get(); }
    public String getConditionItem() { return conditionItem.get(); }
    public String getDescription() { return description.get(); }
    public String getExpectedSwap() { return expectedSwap.get(); }
    public double getPrice() { return price.get(); }
    public String getSellerId() { return sellerId.get(); }
    public String getSellerName() { return sellerName.get(); }
    public String getSellerNumber() { return sellerNumber.get(); }
    public String getFrontImagePath() { return frontImagePath.get(); }
    public String getBackImagePath() { return backImagePath.get(); }
    public String getStatus() { return status.get(); }
}
