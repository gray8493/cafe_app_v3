package com.example.demo.controller;
import java.util.List;
import com.example.demo.dto.OrderRequestDto;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.OrderHistoryDto;
@RestController // Sử dụng @RestController để tự động chuyển đổi phản hồi thành JSON
@RequestMapping("/api/orders") // Endpoint API cho đơn hàng
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
 public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        try {
            // Gọi phương thức mới trong service
            Order newOrUpdatedOrder = orderService.createOrUpdateOrder(orderRequestDto);
            return new ResponseEntity<>(newOrUpdatedOrder, HttpStatus.CREATED);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            // Trả về lỗi cụ thể hơn nếu có thể
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping
    public ResponseEntity<List<OrderHistoryDto>> getAllOrders() {
        try {
            List<OrderHistoryDto> orders = orderService.getAllOrdersForHistory();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            // Log lỗi ở đây
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // Bạn có thể thêm các endpoint GET /api/orders, GET /api/orders/{id} để xem danh sách đơn hàng
    
    /**
     * Xóa một đơn hàng theo ID
     * @param id ID của đơn hàng cần xóa
     * @return 204 No Content nếu xóa thành công, 404 nếu không tìm thấy đơn hàng
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}