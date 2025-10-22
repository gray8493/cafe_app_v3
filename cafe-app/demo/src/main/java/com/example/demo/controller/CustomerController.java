package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.http.HttpStatus;


import org.springframework.web.bind.annotation.*;
import java.util.List; 

// Chú ý: Bạn đã thay đổi RequestMapping ở đây.
// Nếu bạn muốn các API của Customer nằm dưới /api/admin/customers thì giữ nguyên.
// Nếu bạn muốn chúng nằm dưới /api/customers hoặc /api/customer, hãy điều chỉnh @RequestMapping("/api/admin/customers")
// Tuy nhiên, vì AdminController đang phục vụ trang HTML, chúng ta sẽ để Controller này chỉ xử lý API.
// Hoặc nếu bạn muốn giữ nó là @Controller và phục vụ trang, thì AdminController phải không ánh xạ /admin/customers.

@RestController // THAY ĐỔI TỪ @Controller SANG @RestController
@RequestMapping("/api/admin/customers") // Đây sẽ là tiền tố cho các API khách hàng
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

   
    // Lấy tất cả khách hàng (cho AJAX)
    @GetMapping // URL sẽ là /api/admin/customers (do @RequestMapping của lớp)
    // @ResponseBody // Không cần @ResponseBody khi dùng @RestController
    public List<Customer> getAllCustomersApi(@RequestParam(required = false) String search) {
        return customerService.searchCustomers(search);
    }

    // Lấy một khách hàng theo ID
    @GetMapping("/{id}")
    public Customer getCustomerByIdApi(@PathVariable Long id) {
        return customerService.getCustomerById(id); // Giả sử có phương thức getCustomerById trong CustomerService
    }


    // Thêm khách hàng (cho AJAX POST)
    @PostMapping // URL sẽ là /api/admin/customers
    // @ResponseBody
    public Customer addCustomerApi(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    // Cập nhật khách hàng (cho AJAX PUT)
    @PutMapping("/{id}") // URL sẽ là /api/admin/customers/{id}
    // @ResponseBody
    public Customer updateCustomerApi(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id); // Đảm bảo ID được set đúng
        return customerService.saveCustomer(customer);
    }

    // Xóa khách hàng (cho AJAX DELETE)
    @DeleteMapping("/{id}") // URL sẽ là /api/admin/customers/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT) // Trả về 204 No Content khi xóa thành công
    // @ResponseBody
    public void deleteCustomerApi(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    // Các phương thức cũ không dùng AJAX có thể giữ lại hoặc xóa nếu không cần
    // Nếu bạn giữ chúng, hãy đảm bảo chúng không có @ResponseBody
    // hoặc chuyển chúng sang một @Controller riêng chỉ phục vụ HTML form.
    @PostMapping("/update/{id}") // URL: /api/admin/customers/update/{id}
    public String updateCustomer(@PathVariable Long id, @ModelAttribute Customer customer) {
        customer.setId(id);
        customerService.saveCustomer(customer);
        return "redirect:/admin/customers"; // Chuyển hướng về trang HTML
    }
    // Nếu bạn có phương thức getCustomerById trong CustomerService, bạn có thể thêm:
    // public Customer getCustomerById(Long id) {
    //     return customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
    // }
}