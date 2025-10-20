package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    private String image;
   
    private boolean available = true;

    // --- Các trường MỚI và đã điều chỉnh ---

    @Column(nullable = false) // Đảm bảo mọi món đều có danh mục
    private String category; // Ví dụ: "Cafe", "Trà Sữa", "Topping"

   
    private Double priceS; // Giá cho size Nhỏ
    @Column(nullable = false)
    private Double priceM; // Giá cho size Vừa (cơ bản)
    private Double priceL; // Giá cho size Lớn
    // Default constructor
    public MenuItem() {
    }

    // Parameterized constructor
   public MenuItem(String name, String description, String image, boolean available, String category,
                    Double priceS, Double priceM, Double priceL) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.available = available;
        this.category = category;
        this.priceS = priceS;
        this.priceM = priceM;
        this.priceL = priceL;
    }

    // Getters and Setters
   public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    // Getters và Setters cho các trường mới
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getPriceS() { return priceS; }
    public void setPriceS(Double priceS) { this.priceS = priceS; }
    public Double getPriceM() { return priceM; }
    public void setPriceM(Double priceM) { this.priceM = priceM; }
    public Double getPriceL() { return priceL; }
    public void setPriceL(Double priceL) { this.priceL = priceL; }
    
    /**
     * Lấy giá mặc định (size M)
     */
    public Double getPrice() {
        return priceM;
    }
    
    /**
     * Lấy giá theo size cụ thể
     * @param size Kích thước (S, M, L)
     * @return Giá tương ứng với size, mặc định là size M nếu size không hợp lệ
     */
    public Double getPrice(String size) {
        if (size == null) return priceM;
        return switch(size.toUpperCase()) {
            case "S" -> priceS != null ? priceS : priceM;
            case "L" -> priceL != null ? priceL : priceM;
            default -> priceM;
        };
    }
}
