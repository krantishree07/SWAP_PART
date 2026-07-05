package com.example.swapmart.userrecordcontrol;

public class userrecordbean {

    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
    private byte[] photo; // NEW field for photo

    // 🔥 Constructor
    public userrecordbean(int id, String name, String email, String phone, String address, String role, byte[] photo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.photo = photo;
    }

    // ✅ Getters (VERY IMPORTANT for TableView)
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getRole() {
        return role;
    }

    public byte[] getPhoto() {
        return photo;
    }

    // Optional: Setter if you want to update photo later
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
