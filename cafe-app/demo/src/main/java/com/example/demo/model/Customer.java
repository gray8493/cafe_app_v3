package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;

    // Trường mới: địa chỉ khách hàng
    private String address;

    // Trường mới: điểm thưởng khách hàng
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer points = 0;

    // Constructor không tham số (BẮT BUỘC)
    public Customer() {
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    // Phương thức tiện ích để tăng điểm
    public void addPoints(int points) {
        if (this.points == null) {
            this.points = 0;
        }
        this.points += points;
    }
}