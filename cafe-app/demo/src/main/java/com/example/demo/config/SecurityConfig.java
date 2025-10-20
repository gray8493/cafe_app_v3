package com.example.demo.config;

import com.example.demo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
            .csrf(csrf -> csrf.disable()) // Tạm thời tắt CSRF để kiểm tra
            .authorizeHttpRequests(auth -> auth
                // 1. Các URL cho phép truy cập công khai (permitAll)
                .requestMatchers(
                    "/", 
                    "/register", 
                    "/css/**", 
                    "/js/**", 
                    "/images/**",
                    "/static/**",
                    "/webjars/**",
                    "/ws-suggest/**", // Cho phép kết nối WebSocket
                    "/api/suggestions/**" // API gợi ý
                ).permitAll()

                // 2. Các URL yêu cầu xác thực nhưng không cần vai trò cụ thể (authenticated)
                .requestMatchers("/api/orders/**").authenticated() // API đặt hàng
                .requestMatchers(HttpMethod.GET, "/api/admin/menu/grouped").authenticated() // API tải menu cho trang đặt hàng

                // 3. Các URL yêu cầu vai trò USER (hoặc ADMIN)
                .requestMatchers("/admin/menu/order").hasAnyRole("USER", "ADMIN")

                // 4. Các URL chỉ yêu cầu vai trò ADMIN (quy tắc chung hơn)
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Các API admin khác
                .requestMatchers("/admin/**").hasRole("ADMIN")     // Các trang admin khác

                // 5. Bất kỳ yêu cầu nào khác chưa được định nghĩa đều cần xác thực
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