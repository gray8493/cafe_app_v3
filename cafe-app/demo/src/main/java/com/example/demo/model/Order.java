package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference; // THÊM DÒNG IMPORT NÀY

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tableNumber;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String paymentMethod; // Ví dụ: "CASH", "QR"
    private Double cashReceived;  // Số tiền khách đưa
    private Double cashChange;    // Số tiền thối lại
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @JsonManagedReference 
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    private String orderType;
    private String status;

    // Constructors
    public Order() {
        this.orderDate = LocalDateTime.now();
    }

    // Getters và Setters
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public Double getCashReceived() { return cashReceived; }
    public void setCashReceived(Double cashReceived) { this.cashReceived = cashReceived; }
    public Double getCashChange() { return cashChange; }
    public void setCashChange(Double cashChange) { this.cashChange = cashChange; }
    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        for (OrderItem item : orderItems) {
            item.setOrder(this);
        }
    }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Thêm phương thức tiện ích để thêm OrderItem
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }
}