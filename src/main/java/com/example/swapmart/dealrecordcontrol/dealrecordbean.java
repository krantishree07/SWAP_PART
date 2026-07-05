package com.example.swapmart.dealrecordcontrol;

import javafx.beans.property.*;

public class dealrecordbean {
    private final IntegerProperty dealId;
    private final IntegerProperty buyerId;
    private final IntegerProperty sellerId;
    private final IntegerProperty productId;
    private final DoubleProperty amount;
    private final StringProperty dealType;
    private final StringProperty notes;

    public dealrecordbean(int dealId, int buyerId, int sellerId, int productId,
                          double amount, String dealType, String notes) {
        this.dealId = new SimpleIntegerProperty(dealId);
        this.buyerId = new SimpleIntegerProperty(buyerId);
        this.sellerId = new SimpleIntegerProperty(sellerId);
        this.productId = new SimpleIntegerProperty(productId);
        this.amount = new SimpleDoubleProperty(amount);
        this.dealType = new SimpleStringProperty(dealType);
        this.notes = new SimpleStringProperty(notes);
    }

    // Properties
    public IntegerProperty dealIdProperty() { return dealId; }
    public IntegerProperty buyerIdProperty() { return buyerId; }
    public IntegerProperty sellerIdProperty() { return sellerId; }
    public IntegerProperty productIdProperty() { return productId; }
    public DoubleProperty amountProperty() { return amount; }
    public StringProperty dealTypeProperty() { return dealType; }
    public StringProperty notesProperty() { return notes; }

    // Getters
    public int getDealId() { return dealId.get(); }
    public int getBuyerId() { return buyerId.get(); }
    public int getSellerId() { return sellerId.get(); }
    public int getProductId() { return productId.get(); }
    public double getAmount() { return amount.get(); }
    public String getDealType() { return dealType.get(); }
    public String getNotes() { return notes.get(); }
}
