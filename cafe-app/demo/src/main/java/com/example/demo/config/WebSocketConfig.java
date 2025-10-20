package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Kích hoạt WebSocket message handling
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Vai trò 1: Thiết lập "kênh" (topic) để gửi tin nhắn
        // Dòng này nói rằng: "Các tin nhắn có đích đến bắt đầu bằng '/topic' sẽ được gửi đến tất cả các client đã đăng ký."
        // Đây là lý do tại sao trong SuggestionNotifier.java, bạn gửi đến "/topic/suggestions".
        config.enableSimpleBroker("/topic");
        
        // Vai trò 2 (Không dùng trong trường hợp này): Thiết lập tiền tố cho tin nhắn từ client gửi lên
        // Ví dụ: nếu client gửi tin nhắn đến "/app/hello", Spring sẽ tìm một controller có @MessageMapping("/hello").
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Vai trò 3: Tạo "cổng kết nối" (endpoint)
        // Dòng này tạo ra một endpoint tại URL "/ws-suggest" mà client (trang suggestion_display.html) có thể kết nối tới.
        // "withSockJS()" là để hỗ trợ các trình duyệt cũ không có WebSocket gốc.
        registry.addEndpoint("/ws-suggest").withSockJS();
    }
}