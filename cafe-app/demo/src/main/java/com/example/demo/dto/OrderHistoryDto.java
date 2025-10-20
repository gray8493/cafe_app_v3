package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderHistoryDto {
    private Long id;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status;
    private String tableNumber;
    private String orderType;
    private CustomerDto customer;
    private List<OrderItemResponseDto> orderItems;

    // Constructors
    public OrderHistoryDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public CustomerDto getCustomer() { return customer; }
    public void setCustomer(CustomerDto customer) { this.customer = customer; }
    public List<OrderItemResponseDto> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemResponseDto> orderItems) { this.orderItems = orderItems; }
}