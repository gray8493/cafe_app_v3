package com.example.demo.repository;


import com.example.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

 
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'COMPLETED'")
    Double getTotalCompletedRevenue();

    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    
    List<Object[]> countOrdersByStatus();
    
    Optional<Order> findByCustomerIdAndStatus(Long customerId, String status);

    @Query("SELECT oi.itemName, SUM(oi.quantity), SUM(oi.priceAtOrder * oi.quantity) FROM OrderItem oi GROUP BY oi.itemName ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTopSellingMenuItemsByQuantity();

   

   
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0.0) FROM Order o WHERE o.orderDate >= ?1 AND o.orderDate < ?2 AND o.status = 'COMPLETED'")
    Double getDailyRevenue(LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT COUNT(o) FROM Order o WHERE DATE(o.orderDate) = CURRENT_DATE AND o.status = 'COMPLETED'")
    Long countOrdersToday();

    List<Order> findTop5ByOrderByOrderDateDesc();






     List<Order> findByCustomerId(Long customerId);

}