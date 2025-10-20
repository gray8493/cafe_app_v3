package com.example.demo.dto;

public class OrderItemResponseDto {
    private Long id;
    private String itemName;
    private int quantity;
    private double priceAtOrder;
    private String notes;

    // Constructors
    public OrderItemResponseDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPriceAtOrder() { return priceAtOrder; }
    public void setPriceAtOrder(double priceAtOrder) { this.priceAtOrder = priceAtOrder; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}