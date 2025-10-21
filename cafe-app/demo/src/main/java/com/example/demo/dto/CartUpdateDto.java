package com.example.demo.dto;

import java.util.List;

public class CartUpdateDto {
    private List<CartItemDto> items;
    private double totalAmount;

    public CartUpdateDto(List<CartItemDto> items, double totalAmount) {
        this.items = items;
        this.totalAmount = totalAmount;
    }
    public List<CartItemDto> getItems() {
        return items;
    }
    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
}