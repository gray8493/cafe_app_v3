package com.example.demo.dto;

import java.util.List;

public class OrderRequestDto {
    private Long customerId;
    private List<OrderItemDto> items;

    
    private String orderType; 
    private String tableNumber;
    private String paymentMethod;
    private Double cashReceived;
    
    // Getters and Setters
    public String getTableNumber() {
        return tableNumber;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public Double getCashReceived() {
        return cashReceived;
    }
    public void setCashReceived(Double cashReceived) {
        this.cashReceived = cashReceived;
    }
    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

    // Getter và Setter cho trường mới
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}