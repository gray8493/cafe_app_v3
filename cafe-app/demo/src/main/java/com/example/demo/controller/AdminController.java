package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.ui.Model; // Không cần thiết nếu JavaScript tự tải dữ liệu

@Controller
@RequestMapping("/admin")
public class AdminController {

    
    @GetMapping
    public String adminDashboard() {
        return "admin"; // Trả về tên của template trong /templates, Thymeleaf sẽ xử lý nó.
    }

    /**
     * Hiển thị trang Quản lý Menu.
     * URL: GET /admin/menu
     */
    @GetMapping("/menu")
    public String adminMenuManagement() {
        return "menucontrol"; 
    }

    /**
     * Hiển thị trang Menu để đặt hàng.
     * URL: GET /admin/menu/order
     */
    @GetMapping("/menu/order")
    public String adminMenuOrder() {
        return "menu-order"; 
    }

    /**
     * Hiển thị trang Quản lý Khách hàng.
     * URL: GET /admin/customers
     */
    @GetMapping("/customers")
    public String adminCustomerManagement() {
        return "customer"; 
    }
     @GetMapping("/order-history")
    public String showOrderHistoryPage() {
        // Tên này phải khớp với tên tệp trong thư mục templates
        // Ví dụ: src/main/resources/templates/order-history.html
        return "order-history"; 
    }
}