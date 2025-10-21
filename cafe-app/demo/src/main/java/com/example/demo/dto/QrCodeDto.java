package com.example.demo.dto;

public class QrCodeDto {
    private String qrImageUrl;
    private double totalAmount;

    // Constructors
    public QrCodeDto() {} // Constructor rỗng

    public QrCodeDto(String qrImageUrl, double totalAmount) {
        this.qrImageUrl = qrImageUrl;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public String getQrImageUrl() {
        return qrImageUrl;
    }

    public void setQrImageUrl(String qrImageUrl) {
        this.qrImageUrl = qrImageUrl;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}