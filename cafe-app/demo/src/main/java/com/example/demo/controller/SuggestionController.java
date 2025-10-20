package com.example.demo.controller;

import com.example.demo.dto.FaceAnalysisResultDto;
import com.example.demo.dto.ImageAnalysisRequestDto;
import com.example.demo.dto.SuggestionResponseDto;
import com.example.demo.service.GoogleAiService;
import com.example.demo.service.SuggestionNotifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {

    private final GoogleAiService googleAiService;
    private final SuggestionNotifier suggestionNotifier;
    private final ObjectMapper objectMapper; // Chỉ khai báo, không khởi tạo

    // SỬA ĐỔI CONSTRUCTOR ĐỂ NHẬN TẤT CẢ DEPENDENCY
    public SuggestionController(GoogleAiService googleAiService, 
                                SuggestionNotifier suggestionNotifier,
                                ObjectMapper objectMapper) { // Để Spring tự tiêm ObjectMapper
        this.googleAiService = googleAiService;
        this.suggestionNotifier = suggestionNotifier;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/analyze-face")
    public ResponseEntity<SuggestionResponseDto> getSuggestion(@RequestBody ImageAnalysisRequestDto request) {
        try {
            // 1. Gửi ảnh đến Google AI Service
            String jsonResult = googleAiService.analyzeImageForSuggestions(request.getImageBase64());
            
            // 2. Parse kết quả JSON
            FaceAnalysisResultDto analysisResult = objectMapper.readValue(jsonResult, FaceAnalysisResultDto.class);

            // 3. Logic đưa ra gợi ý
            String suggestionText = generateSuggestion(analysisResult.getAge(), analysisResult.getEmotion());
            
            // 4. Tạo đối tượng phản hồi
            SuggestionResponseDto responseDto = new SuggestionResponseDto(
                analysisResult.getAge(), 
                analysisResult.getEmotion(), 
                suggestionText
            );
            
            // 5. Đẩy gợi ý đến màn hình hiển thị qua WebSocket
            suggestionNotifier.notifySuggestion(responseDto);
            
            // 6. Trả về phản hồi cho chương trình Python client
            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            e.printStackTrace();
            // Trả về một phản hồi lỗi chi tiết hơn
            return ResponseEntity.internalServerError().body(null); 
        }
    }

    private String generateSuggestion(int age, String emotion) {
        if (age < 18) {
            if ("vui vẻ".equals(emotion) || "ngạc nhiên".equals(emotion)) {
                return "Một ly Trà sữa trân châu đường đen sẽ rất tuyệt!";
            }
            return "Trà đào cam sả thanh mát nhé?";
        } else if (age >= 18 && age < 30) {
            if ("vui vẻ".equals(emotion)) {
                return "Bạn trông thật năng động! Thử ngay Cà phê sữa đá của chúng tôi!";
            }
            if ("buồn".equals(emotion) || "trung tính".equals(emotion)) {
                return "Một ly Bạc xỉu nóng sẽ giúp bạn thư giãn hơn.";
            }
            return "Cà phê muối đang là trend đó, bạn thử không?";
        } else {
            if ("trung tính".equals(emotion)) {
                return "Thưởng thức một ly Cà phê đen nguyên chất nhé?";
            }
            return "Một ly Trà sen vàng sẽ giúp bạn sảng khoái tinh thần.";
        }
    }
}