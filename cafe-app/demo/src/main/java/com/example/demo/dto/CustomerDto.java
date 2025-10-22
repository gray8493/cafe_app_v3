package com.example.demo.dto;

public class CustomerDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Integer points;

    // Constructors
    public CustomerDto() {}

    public CustomerDto(Long id, String name) {
        this.id = id;
        this.name = name;
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
}