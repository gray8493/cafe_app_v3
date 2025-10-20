package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Đại diện cho một món ăn cụ thể trong một đơn hàng (Order).
 * Ví dụ: 2 ly "Cà phê sữa (Size M)".
 */
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Mối quan hệ ngược lại với Order. Mỗi OrderItem thuộc về một Order.
     * @JsonBackReference được sử dụng để tránh lỗi lặp vô hạn khi tuần tự hóa sang JSON.
     */
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * Mối quan hệ với MenuItem gốc.
     * Khi MenuItem gốc bị xóa, trường này sẽ được đặt thành NULL để bảo toàn lịch sử đơn hàng.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id") // Cột này trong CSDL phải cho phép giá trị NULL
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private MenuItem menuItem;

    // Các trường sau đây là "bản sao" thông tin tại thời điểm đặt hàng.
    // Điều này đảm bảo rằng lịch sử đơn hàng không thay đổi ngay cả khi menu thay đổi (ví dụ: đổi tên, đổi giá).

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Double priceAtOrder;

    @Column(nullable = false)
    private Integer quantity;

    private String selectedSize; // Ví dụ: "S", "M", "L"

    @Column(columnDefinition = "TEXT")
    private String notes; // Ví dụ: "Ít đường", "Không đá"

    // Constructor rỗng là bắt buộc đối với JPA
    public OrderItem() {}

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getPriceAtOrder() {
        return priceAtOrder;
    }

    public void setPriceAtOrder(Double priceAtOrder) {
        this.priceAtOrder = priceAtOrder;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

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
}