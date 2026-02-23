package com.donasi.donasiapi;

public class AddNodeRequest {
    // Nama parent yang mau kita tambahin anak
    private String parentName;

    // Info node baru
    private String name;
    private String type;
    private String desc;

    // Field BARU untuk nominal donasi
    private double amount;

    // ======================================
    // 1. KONSTRUKTOR (Opsional, tapi disarankan)
    // ======================================

    // Konstruktor default (tanpa argumen)
    public AddNodeRequest() {
    }

    // Konstruktor dengan semua field (untuk kemudahan inisialisasi)
    public AddNodeRequest(String parentName, String name, String type, String desc, double amount) {
        this.parentName = parentName;
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.amount = amount;
    }

    // ======================================
    // 2. GETTER dan SETTER (Pengganti @Data Lombok)
    // ======================================

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Catatan: Jika Anda perlu method toString(), equals(), dan hashCode()
    // (yang juga disediakan oleh @Data), Anda harus membuatnya secara manual di sini.
}