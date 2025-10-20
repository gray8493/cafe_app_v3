package com.example.demo.dto;

import java.util.List;

public class OrderItemDto {
    private Long menuItemId;
    private Integer quantity;

    // --- Các trường MỚI ---
    private String selectedSize; // Ví dụ: "S", "M", "L"
    private String notes;
    private List<Long> toppingIds; // Danh sách ID của các topping được chọn (vì topping cũng là MenuItem)

    // Getters and Setters
    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // Getters và Setters cho các trường mới
    public String getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Long> getToppingIds() {
        return toppingIds;
    }

    public void setToppingIds(List<Long> toppingIds) {
        this.toppingIds = toppingIds;
    }
}