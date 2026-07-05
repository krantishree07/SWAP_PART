package com.example.swapmart.interestrecordcontrol;

import javafx.beans.property.*;
import java.sql.Timestamp;

public class interestrecordbean {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty phone;
    private final StringProperty message;
    private final StringProperty address;
    private final ObjectProperty<Timestamp> createdAt;

    public interestrecordbean(int id, String name, String phone, String message, String address, Timestamp createdAt) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.message = new SimpleStringProperty(message);
        this.address = new SimpleStringProperty(address);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
    }

    // --- ID ---
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    // --- Name ---
    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    // --- Phone ---
    public String getPhone() { return phone.get(); }
    public void setPhone(String value) { phone.set(value); }
    public StringProperty phoneProperty() { return phone; }

    // --- Message ---
    public String getMessage() { return message.get(); }
    public void setMessage(String value) { message.set(value); }
    public StringProperty messageProperty() { return message; }

    // --- Address ---
    public String getAddress() { return address.get(); }
    public void setAddress(String value) { address.set(value); }
    public StringProperty addressProperty() { return address; }

    // --- CreatedAt ---
    public Timestamp getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(Timestamp value) { createdAt.set(value); }
    public ObjectProperty<Timestamp> createdAtProperty() { return createdAt; }
}
