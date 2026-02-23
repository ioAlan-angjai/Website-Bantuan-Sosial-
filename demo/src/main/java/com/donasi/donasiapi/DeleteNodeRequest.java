package com.donasi.donasiapi;

public class DeleteNodeRequest {
    private String name;

    // Konstruktor default
    public DeleteNodeRequest() {
    }

    // Konstruktor dengan field
    public DeleteNodeRequest(String name) {
        this.name = name;
    }

    // Getter dan Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}