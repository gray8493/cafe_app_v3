package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.ui.Model;

@Controller // Đây là controller để phục vụ các view HTML
public class ViewController {

    // Ví dụ các ánh xạ view chung hoặc công khai
    @GetMapping("/")
    public String homePage() {
        return "redirect:/login"; // Hoặc một trang chào mừng nào đó
    }

    /**
     * Hiển thị trang Quản lý Đơn Hàng.
     * URL: /order-management
     * Template: order-management.html
     */
    @GetMapping("/order-management")
    public String showOrderManagementPage() {
        return "order-management"; // Trả về template order-management.html
    }

    /**
     * Hiển thị trang Thống kê Đơn Hàng.
     * URL: /order-statistics
     * Template: order-statistics.html
     */
    @GetMapping("/order-statistics")
    public String showOrderStatisticsPage() {
        return "order-statistics"; // Trả về template order-statistics.html
    }

    // Ghi chú: Các ánh xạ /admin, /admin/menu, /admin/customers đã được xử lý bởi AdminController.java
}