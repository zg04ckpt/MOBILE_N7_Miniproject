package com.hoangcn.n7.models;

import java.io.Serializable;

public class Room implements Serializable {
    private int id;
    private String name;
    private float price;
    private String status;
    private String tenant;
    private String phoneNumber;
    private String imageUri;

    public Room() {}

    public Room(int id, String name, float price, String status, String tenant, String phoneNumber, String imageUri) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.status = status;
        this.tenant = tenant;
        this.phoneNumber = phoneNumber;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
