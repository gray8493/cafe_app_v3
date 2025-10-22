package com.example.demo.controller;

import com.example.demo.dto.CartUpdateDto;
import com.example.demo.dto.FaceAnalysisResultDto;
import com.example.demo.dto.ImageAnalysisRequestDto;
import com.example.demo.dto.QrCodeDto; // <-- THÊM IMPORT MỚI
import com.example.demo.dto.SuggestionResponseDto;
import com.example.demo.model.MenuItem;
import com.example.demo.service.GoogleAiService;
import com.example.demo.service.MenuService;
import com.example.demo.service.SuggestionNotifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Controller này xử lý các API liên quan đến màn hình hiển thị cho khách hàng.
 */
@RestController
@RequestMapping("/api/customer-display")
public class SuggestionController {

    private final GoogleAiService googleAiService;
    private final SuggestionNotifier suggestionNotifier;
    private final ObjectMapper objectMapper;
    private final MenuService menuService;

    public SuggestionController(GoogleAiService googleAiService,
                                SuggestionNotifier suggestionNotifier,
                                ObjectMapper objectMapper,
                                MenuService menuService) {
        this.googleAiService = googleAiService;
        this.suggestionNotifier = suggestionNotifier;
        this.objectMapper = objectMapper;
        this.menuService = menuService;
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
            return ResponseEntity.internalServerError().body(new SuggestionResponseDto(0, "error", "Lỗi xử lý ảnh: " + e.getMessage()));
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
     * Logic tạo gợi ý thông minh dựa trên tuổi, cảm xúc và các món từ menu thực tế.
     * Chọn ngẫu nhiên từ danh sách các món phù hợp.
     */
    private String generateSuggestion(int age, String emotion) {
        try {
            // Lấy tất cả món uống từ menu (không phải topping)
            List<MenuItem> drinkableItems = menuService.getDrinkableItems();

            if (drinkableItems.isEmpty()) {
                // Fallback nếu không có menu
                return generateFallbackSuggestion(age, emotion);
            }

            // Lọc món dựa trên tuổi và cảm xúc
            List<MenuItem> suitableItems = filterItemsByAgeAndEmotion(drinkableItems, age, emotion);

            // Nếu không có món phù hợp, lấy tất cả món
            if (suitableItems.isEmpty()) {
                suitableItems = drinkableItems;
            }

            // Chọn ngẫu nhiên một món
            MenuItem selectedItem = suitableItems.get(new java.util.Random().nextInt(suitableItems.size()));

            // Tạo gợi ý với tên món
            return generatePersonalizedSuggestion(selectedItem, age, emotion);

        } catch (Exception e) {
            // Fallback nếu có lỗi
            return generateFallbackSuggestion(age, emotion);
        }
    }

    /**
     * Lọc danh sách món dựa trên tuổi và cảm xúc
     */
    private List<MenuItem> filterItemsByAgeAndEmotion(List<MenuItem> items, int age, String emotion) {
        return items.stream()
            .filter(item -> {
                String category = item.getCategory();
                String name = item.getName().toLowerCase();

                // Phân loại dựa trên tuổi
                if (age < 18) {
                    // Trà sữa, nước trái cây cho trẻ em/vị thành niên
                    return category.contains("Trà") || category.contains("Nước") ||
                           name.contains("trà sữa") || name.contains("nước ép");
                } else if (age >= 18 && age < 30) {
                    // Cafe, đồ uống năng động cho người trẻ
                    return category.contains("Cafe") || category.contains("Cà phê") ||
                           name.contains("cafe") || name.contains("cà phê");
                } else {
                    // Đồ uống truyền thống, trà cho người lớn tuổi
                    return category.contains("Trà") || name.contains("trà") ||
                           name.contains("café đen") || name.contains("cà phê đen");
                }
            })
            .filter(item -> {
                // Phân loại dựa trên cảm xúc
                String name = item.getName().toLowerCase();
                if ("vui vẻ".equals(emotion) || "ngạc nhiên".equals(emotion)) {
                    // Đồ uống vui vẻ, sáng tạo
                    return name.contains("sữa") || name.contains("đá") || name.contains("trái cây");
                } else if ("buồn".equals(emotion) || "trung tính".equals(emotion)) {
                    // Đồ uống ấm áp, thư giãn
                    return name.contains("nóng") || name.contains("sữa") || name.contains("trà");
                } else {
                    // Đồ uống trung tính
                    return true; // Chấp nhận tất cả
                }
            })
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Tạo gợi ý cá nhân hóa dựa trên món được chọn
     */
    private String generatePersonalizedSuggestion(MenuItem item, int age, String emotion) {
        StringBuilder suggestion = new StringBuilder();

        // Phần chào hỏi dựa trên cảm xúc
        if ("vui vẻ".equals(emotion) || "ngạc nhiên".equals(emotion)) {
            suggestion.append("Bạn trông thật vui vẻ! ");
        } else if ("buồn".equals(emotion)) {
            suggestion.append("Hãy thư giãn với ");
        } else {
            suggestion.append("Thử ngay ");
        }

        // Thêm tên món
        suggestion.append(item.getName());

        // Thêm mô tả nếu có
        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            suggestion.append(" - ").append(item.getDescription());
        }

        // Thêm giá nếu có
        if (item.getPrice() != null) {
            suggestion.append(" (").append(item.getPrice().intValue()).append("k)");
        }

        suggestion.append(" nhé!");

        return suggestion.toString();
    }

    /**
     * Gợi ý fallback khi không có menu hoặc có lỗi
     */
    private String generateFallbackSuggestion(int age, String emotion) {
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