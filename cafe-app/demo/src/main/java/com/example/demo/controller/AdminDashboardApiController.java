package com.example.demo.controller;

import com.example.demo.dto.DashboardStatsDto; // THÊM IMPORT NÀY
import com.example.demo.service.OrderService; // Sử dụng OrderService để lấy dữ liệu thống kê
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Đánh dấu đây là một REST Controller để trả về JSON
@RequestMapping("/api/admin/dashboard") // Endpoint API cho bảng điều khiển admin
public class AdminDashboardApiController {

    private final OrderService orderService; // Inject OrderService

    public AdminDashboardApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/stats") // Endpoint cụ thể để lấy thống kê bảng điều khiển
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        DashboardStatsDto stats = orderService.getDashboardStatistics();
        return ResponseEntity.ok(stats);
    }
}
