package com.example.demo.dto;

public class MenuItemSaleStatsDto {
    private String itemName;
    private Long totalQuantitySold;
    private Double totalRevenue;

    // Constructor để dễ dàng tạo đối tượng từ kết quả truy vấn
    public MenuItemSaleStatsDto(String itemName, Long totalQuantitySold, Double totalRevenue) {
        this.itemName = itemName;
        this.totalQuantitySold = totalQuantitySold;
        this.totalRevenue = totalRevenue;
    }

    // Constructor mặc định (cần thiết cho quá trình deserialization của Jackson nếu có)
    public MenuItemSaleStatsDto() {
    }

    // Getters and Setters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getTotalQuantitySold() {
        return totalQuantitySold;
    }

    public void setTotalQuantitySold(Long totalQuantitySold) {
        this.totalQuantitySold = totalQuantitySold;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}