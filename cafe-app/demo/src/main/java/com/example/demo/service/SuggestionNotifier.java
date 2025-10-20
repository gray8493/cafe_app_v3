package com.example.demo.service;

import com.example.demo.dto.SuggestionResponseDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SuggestionNotifier {

    private final SimpMessagingTemplate messagingTemplate;
    
    // Đường dẫn WebSocket endpoint mà client sẽ subscribe
    private static final String WEBSOCKET_TOPIC = "/topic/suggestions";

    public SuggestionNotifier(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Gửi thông báo gợi ý đến tất cả các client đang kết nối
     * @param suggestionResponseDto Đối tượng chứa thông tin gợi ý
     */
    public void notifySuggestion(SuggestionResponseDto suggestionResponseDto) {
        // Gửi message đến tất cả các client đang subscribe vào topic /topic/suggestions
        messagingTemplate.convertAndSend(WEBSOCKET_TOPIC, suggestionResponseDto);
    }
}
