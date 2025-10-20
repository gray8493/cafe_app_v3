package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Thêm import này

import java.util.List;

@Service  
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository; // Bỏ @Autowired ở đây

    // Constructor đã đúng, Spring sẽ tự động inject cả hai repository
    public CustomerService(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khách hàng với ID: " + id));
    }
 
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional // Thêm @Transactional để đảm bảo tất cả các thao tác CSDL được thực hiện trong một giao dịch
    public void deleteCustomer(Long id) {
        // Lấy khách hàng vãng lai (ID = 1)
        Customer guestCustomer = customerRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khách hàng vãng lai (ID=1)."));

        // Tìm tất cả các đơn hàng của khách hàng sắp bị xóa
        // Sửa: Gọi phương thức từ biến instance 'orderRepository' (chữ 'o' viết thường)
        List<Order> customerOrders = orderRepository.findByCustomerId(id); 
        
        // Gán lại các đơn hàng đó cho khách vãng lai
        for (Order order : customerOrders) {
            order.setCustomer(guestCustomer);
        }
        
        // Sửa: Gọi phương thức từ biến instance 'orderRepository'
        orderRepository.saveAll(customerOrders);

        // Bây giờ mới xóa khách hàng
        // Sửa: Xóa khách hàng có ID được truyền vào, không phải guestCustomer
        customerRepository.deleteById(id);
    }
}