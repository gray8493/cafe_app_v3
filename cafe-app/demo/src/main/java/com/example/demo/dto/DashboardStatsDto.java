package com.example.demo.dto;

import java.util.List;

public class DashboardStatsDto {
    private Long totalOrders;
    private Double todayRevenue;
    private Long totalProducts;
    private Long totalCustomers;
    private List<LatestOrderDto> latestOrders;
    private List<MenuItemSaleStatsDto> topSellingProducts; // Tái sử dụng DTO từ phần thống kê

    public DashboardStatsDto(Long totalOrders, Double todayRevenue, Long totalProducts, Long totalCustomers,
                             List<LatestOrderDto> latestOrders, List<MenuItemSaleStatsDto> topSellingProducts) {
        this.totalOrders = totalOrders;
        this.todayRevenue = todayRevenue;
        this.totalProducts = totalProducts;
        this.totalCustomers = totalCustomers;
        this.latestOrders = latestOrders;
        this.topSellingProducts = topSellingProducts;
    }

    // Getters and Setters
    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Double getTodayRevenue() {
        return todayRevenue;
    }

    public void setTodayRevenue(Double todayRevenue) {
        this.todayRevenue = todayRevenue;
    }

    public Long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public Long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public List<LatestOrderDto> getLatestOrders() {
        return latestOrders;
    }

    public void setLatestOrders(List<LatestOrderDto> latestOrders) {
        this.latestOrders = latestOrders;
    }

    public List<MenuItemSaleStatsDto> getTopSellingProducts() {
        return topSellingProducts;
    }

    public void setTopSellingProducts(List<MenuItemSaleStatsDto> topSellingProducts) {
        this.topSellingProducts = topSellingProducts;
    }
}