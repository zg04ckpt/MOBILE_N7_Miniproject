package com.hoangcn.n7.models;

import java.io.Serializable;

public class Room implements Serializable {
    private String id;
    private String name;
    private double price;
    private String status;
    private String tenantName;
    private String tenantPhone;
    private int previewImage;

    public Room(String id, String name, double price, String status, String tenantName, String tenantPhone, int previewImage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.status = status;
        this.tenantName = tenantName;
        this.tenantPhone = tenantPhone;
        this.previewImage = previewImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantPhone() {
        return tenantPhone;
    }

    public void setTenantPhone(String tenantPhone) {
        this.tenantPhone = tenantPhone;
    }

    public int getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(int previewImage) {
        this.previewImage = previewImage;
    }
}
