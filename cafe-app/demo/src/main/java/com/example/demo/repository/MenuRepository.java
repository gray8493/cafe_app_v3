package com.example.demo.repository;

import com.example.demo.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface để thực hiện các thao tác CRUD trên MenuItem entity.
 * Mở rộng JpaRepository để tự động có các phương thức cơ bản.
 */
@Repository
public interface MenuRepository extends JpaRepository<MenuItem, Long> {

    /**
     * Tự động tạo truy vấn để tìm tất cả các MenuItem thuộc một danh mục cụ thể,
     * và sắp xếp kết quả theo tên (thuộc tính 'name') theo thứ tự tăng dần (Asc).
     * Dùng để lấy danh sách các món topping.
     * 
     * @param category Tên danh mục cần tìm.
     * @return Một danh sách các MenuItem.
     */
    List<MenuItem> findByCategoryOrderByNameAsc(String category);

    /**
     * Tự động tạo truy vấn để tìm tất cả các MenuItem KHÔNG thuộc một danh mục cụ thể,
     * và sắp xếp kết quả theo tên theo thứ tự tăng dần.
     * Dùng để lấy danh sách các món có thể uống/ăn (không phải topping).
     * 
     * @param category Tên danh mục cần loại trừ.
     * @return Một danh sách các MenuItem.
     */
    List<MenuItem> findByCategoryNotOrderByNameAsc(String category);
}