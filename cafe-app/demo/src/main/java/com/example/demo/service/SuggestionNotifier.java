package com.example.demo.service;

import com.example.demo.dto.CartUpdateDto;
import com.example.demo.dto.QrCodeDto;
import com.example.demo.dto.SuggestionResponseDto;
import com.example.demo.dto.WebSocketMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component; // Thay @Service bằng @Component cho phù hợp hơn

/**
 * Lớp này chịu trách nhiệm đẩy thông báo đến các client đã kết nối
 * qua WebSocket. Nó hoạt động như một cầu nối giữa logic nghiệp vụ
 * và các client thời gian thực.
 */
@Component // @Component là một lựa chọn tốt hơn @Service cho các lớp tiện ích chung
public class SuggestionNotifier {

    private final SimpMessagingTemplate template;
    
    // Sử dụng một topic chung cho tất cả các thông báo đến màn hình khách hàng
    private static final String CUSTOMER_DISPLAY_TOPIC = "/topic/customer-display";

    public SuggestionNotifier(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Gửi thông báo gợi ý đồ uống đến màn hình khách hàng.
     * Tin nhắn sẽ có type là "SUGGESTION".
     * @param suggestion DTO chứa thông tin gợi ý (tuổi, cảm xúc, tên món).
     */
    public void notifySuggestion(SuggestionResponseDto suggestion) {
        // Bọc dữ liệu trong một đối tượng WebSocketMessage chung
        WebSocketMessage<SuggestionResponseDto> message = new WebSocketMessage<>("SUGGESTION", suggestion);
        
        // Gửi tin nhắn đến topic
        template.convertAndSend(CUSTOMER_DISPLAY_TOPIC, message);
        
        System.out.println("Đã đẩy gợi ý qua WebSocket: " + suggestion.getSuggestion());
    }
    
    /**
     * Gửi thông báo cập nhật giỏ hàng đến màn hình khách hàng.
     * Tin nhắn sẽ có type là "CART_UPDATE".
     * @param cartUpdate DTO chứa danh sách các món trong giỏ và tổng tiền.
     */
    public void notifyCartUpdate(CartUpdateDto cartUpdate) {
        // Bọc dữ liệu trong một đối tượng WebSocketMessage chung
        WebSocketMessage<CartUpdateDto> message = new WebSocketMessage<>("CART_UPDATE", cartUpdate);
        
        // Gửi tin nhắn đến topic
        template.convertAndSend(CUSTOMER_DISPLAY_TOPIC, message);

        System.out.println("Đã đẩy cập nhật giỏ hàng qua WebSocket. Tổng tiền: " + cartUpdate.getTotalAmount());
    }
     public void notifyShowQrCode(QrCodeDto qrCodeDto) {
        WebSocketMessage<QrCodeDto> message = new WebSocketMessage<>("SHOW_QR_CODE", qrCodeDto);
        template.convertAndSend(CUSTOMER_DISPLAY_TOPIC, message);
        System.out.println("Đã đẩy yêu cầu hiển thị QR qua WebSocket.");
    }
}