package com.donasi.donasiapi;

public class UpdateNodeRequest {
    private String originalName;

    // Data BARU yang akan menggantikannya
    private String newName;
    private String newType;
    private String newDesc;

    // Field untuk nominal donasi (Duit/Barang)
    private double newAmount;

    // Konstruktor default
    public UpdateNodeRequest() {
    }

    // Konstruktor dengan semua field
    public UpdateNodeRequest(String originalName, String newName, String newType, String newDesc, double newAmount) {
        this.originalName = originalName;
        this.newName = newName;
        this.newType = newType;
        this.newDesc = newDesc;
        this.newAmount = newAmount;
    }

    // Getter dan Setter
    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getNewType() {
        return newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }

    public String getNewDesc() {
        return newDesc;
    }

    public void setNewDesc(String newDesc) {
        this.newDesc = newDesc;
    }

    public double getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(double newAmount) {
        this.newAmount = newAmount;
    }
}