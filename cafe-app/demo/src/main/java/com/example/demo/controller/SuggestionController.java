package com.example.demo.controller;

import com.example.demo.dto.CartUpdateDto;
import com.example.demo.dto.FaceAnalysisResultDto;
import com.example.demo.dto.ImageAnalysisRequestDto;
import com.example.demo.dto.QrCodeDto; // <-- THÊM IMPORT MỚI
import com.example.demo.dto.SuggestionResponseDto;
import com.example.demo.service.GoogleAiService;
import com.example.demo.service.SuggestionNotifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Controller này xử lý các API liên quan đến màn hình hiển thị cho khách hàng.
 */
@RestController
@RequestMapping("/api/customer-display")
public class SuggestionController {

    private final GoogleAiService googleAiService;
    private final SuggestionNotifier suggestionNotifier;
    private final ObjectMapper objectMapper;

    public SuggestionController(GoogleAiService googleAiService, 
                                SuggestionNotifier suggestionNotifier,
                                ObjectMapper objectMapper) {
        this.googleAiService = googleAiService;
        this.suggestionNotifier = suggestionNotifier;
        this.objectMapper = objectMapper;
    }

    /**
     * Nhận ảnh từ client Python, phân tích và đẩy gợi ý qua WebSocket.
     * URL: POST /api/customer-display/analyze-face
     */
    @PostMapping("/analyze-face")
    public ResponseEntity<SuggestionResponseDto> getSuggestion(@RequestBody ImageAnalysisRequestDto request) {
        try {
            String jsonResult = googleAiService.analyzeImageForSuggestions(request.getImageBase64());
            FaceAnalysisResultDto analysisResult = objectMapper.readValue(jsonResult, FaceAnalysisResultDto.class);
            String suggestionText = generateSuggestion(analysisResult.getAge(), analysisResult.getEmotion());
            SuggestionResponseDto responseDto = new SuggestionResponseDto(
                analysisResult.getAge(), 
                analysisResult.getEmotion(), 
                suggestionText
            );
            suggestionNotifier.notifySuggestion(responseDto);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null); 
        }
    }
    
    /**
     * Nhận thông tin cập nhật giỏ hàng và đẩy nó đến màn hình khách hàng.
     * URL: POST /api/customer-display/update-cart
     */
    @PostMapping("/update-cart")
    public ResponseEntity<Void> updateCustomerCart(@RequestBody CartUpdateDto cartUpdate) {
        try {
            suggestionNotifier.notifyCartUpdate(cartUpdate);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // =======================================================
    // ==           ENDPOINT MỚI CHO MÃ QR CODE             ==
    // =======================================================
    /**
     * Nhận yêu cầu tạo mã QR từ trang của nhân viên và đẩy đến màn hình khách hàng.
     * URL: POST /api/customer-display/generate-qr
     */
    @PostMapping("/generate-qr")
    public ResponseEntity<Void> generateQrCode(@RequestBody CartUpdateDto cartData) {
        try {
            // --- CẤU HÌNH THÔNG TIN NGÂN HÀNG CỦA BẠN ---
            final String bankId = "970415"; // Ví dụ: VietinBank
            final String accountNumber = "0837474615"; // SỐ TÀI KHOẢN CỦA BẠN
            final String description = "HD" + System.currentTimeMillis();

            double totalAmount = cartData.getTotalAmount();

            // Tạo URL hình ảnh mã QR từ vietqr.io
            String encodedDescription = URLEncoder.encode(description, StandardCharsets.UTF_8);
            String qrImageUrl = String.format(
                "https://img.vietqr.io/image/%s-%s-print.png?amount=%.0f&addInfo=%s",
                bankId,
                accountNumber,
                totalAmount,
                encodedDescription
            );

            // Tạo DTO để gửi đi
            QrCodeDto qrCodeDto = new QrCodeDto(qrImageUrl, totalAmount);
            
            // Đẩy thông tin QR đến màn hình khách hàng qua WebSocket
            suggestionNotifier.notifyShowQrCode(qrCodeDto);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Logic đơn giản để tạo gợi ý dựa trên tuổi và cảm xúc.
     */
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