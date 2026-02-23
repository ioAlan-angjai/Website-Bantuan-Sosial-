package com.donasi.donasiapi;

public class DonationSummary {
    private double totalUangTerkumpul = 0;
    private double totalPcsMakanan = 0;
    private int jumlahDonatur = 0;

    // Getter dan Setter
    public double getTotalUangTerkumpul() {
        return totalUangTerkumpul;
    }

    public void setTotalUangTerkumpul(double totalUangTerkumpul) {
        this.totalUangTerkumpul = totalUangTerkumpul;
    }

    public double getTotalPcsMakanan() {
        return totalPcsMakanan;
    }

    public void setTotalPcsMakanan(double totalPcsMakanan) {
        this.totalPcsMakanan = totalPcsMakanan;
    }

    public int getJumlahDonatur() {
        return jumlahDonatur;
    }

    public void setJumlahDonatur(int jumlahDonatur) {
        this.jumlahDonatur = jumlahDonatur;
    }
}