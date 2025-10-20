# Ứng Dụng Quản Lý Quán Cà Phê

## Mô tả
Ứng dụng quản lý quán cà phê được phát triển bằng Java Spring Boot, giúp quản lý thực đơn, đơn hàng và các hoạt động khác của quán.

## Tính năng chính
- Quản lý thực đơn (thêm, sửa, xóa món)
- Quản lý đơn hàng
- Quản lý bàn
- Thống kê doanh thu

## Yêu cầu hệ thống
- Java 17 hoặc cao hơn
- Maven 3.8+
- MySQL 8.0+

## Cài đặt
1. Clone dự án:
   ```bash
   git clone [đường_dẫn_đến_repository]
   ```
2. Cấu hình cơ sở dữ liệu trong file `application.properties`
3. Chạy ứng dụng:
   ```bash
   mvn spring-boot:run
   ```

## Cấu trúc thư mục
```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   └── model/
│   └── resources/
└── test/
```

## Giấy phép
Dự án được phân phối dưới giấy phép MIT.