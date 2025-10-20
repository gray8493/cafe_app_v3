package com.example.demo.config;

import com.example.demo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public SecurityConfig(CustomUserDetailsService userDetailsService, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Tạm thời tắt CSRF để kiểm tra
            .authorizeHttpRequests(auth -> auth
                // 1. Cho phép truy cập công khai
                .requestMatchers(
                    "/", 
                    "/register", 
                    "/css/**", 
                    "/js/**", 
                    "/images/**",
                    "/static/**", // Bao gồm cả suggestion_display.html
                    "/webjars/**",
                    "/ws-suggest/**" // Cho phép kết nối WebSocket
                ).permitAll()

                // 2. Phân quyền cho API
                // API đặt hàng yêu cầu đã đăng nhập (cả ADMIN và USER đều có thể)
                .requestMatchers("/api/orders/**").authenticated() 
                // API dashboard chỉ dành cho ADMIN
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // API gợi ý có thể cho phép tất cả (nếu bạn muốn) hoặc yêu cầu đăng nhập
                .requestMatchers("/api/suggestions/**").permitAll()

                // 3. Phân quyền cho các trang giao diện (/admin)
                // USER chỉ được vào trang đặt hàng
                .requestMatchers("/admin/menu/order").hasAnyRole("USER", "ADMIN")
                // ADMIN có thể vào tất cả các trang còn lại dưới /admin/
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // 4. Tất cả các yêu cầu khác đều cần đăng nhập
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }
}