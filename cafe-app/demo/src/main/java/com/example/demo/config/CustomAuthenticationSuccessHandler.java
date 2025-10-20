

package com.example.demo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component // Đảm bảo Spring có thể tìm thấy và tạo bean cho lớp này
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Kiểm tra xem người dùng có vai trò ADMIN không
        boolean isAdmin = authorities.stream()
                                     .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Kiểm tra xem người dùng có vai trò USER không
        boolean isUser = authorities.stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

        if (isAdmin) {
            // Nếu là ADMIN, chuyển hướng đến trang dashboard admin
            response.sendRedirect(request.getContextPath() + "/admin");
        } else if (isUser) {
            // Nếu là USER (và không phải ADMIN), chuyển hướng đến trang đặt hàng
            response.sendRedirect(request.getContextPath() + "/admin/menu/order");
        } else {
            // Trường hợp không có vai trò cụ thể hoặc vai trò khác, chuyển hướng về trang chủ
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
