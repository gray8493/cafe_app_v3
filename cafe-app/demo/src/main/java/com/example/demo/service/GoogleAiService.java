package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GoogleAiService {

    @Value("${google.api.key}") // Lấy API Key từ application.properties
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String analyzeImageForSuggestions(String imageBase64) throws IOException {
        

// DÒNG ĐÃ SỬA, SỬ DỤNG API PHIÊN BẢN v1
String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro-vision:generateContent?key=" + apiKey;
        // Tạo headers cho request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo prompt (câu lệnh) cho Gemini
        String prompt = "Phân tích người trong ảnh. Trả về kết quả dưới dạng một đối tượng JSON duy nhất với các trường sau: " +
                        "'age' (số tuổi ước tính, là một số nguyên), " +
                        "'emotion' (cảm xúc chính, chọn một trong các giá trị: 'vui vẻ', 'buồn', 'trung tính', 'ngạc nhiên', 'tức giận'). " +
                        "Chỉ trả về đối tượng JSON, không thêm bất kỳ văn bản giải thích hay định dạng markdown nào.";

        // Tạo phần "parts" của request body, bao gồm cả text prompt và ảnh
        List<Map<String, Object>> parts = List.of(
            Map.of("text", prompt),
            Map.of("inline_data", Map.of(
                "mime_type", "image/jpeg",
                "data", imageBase64
            ))
        );

        // Tạo request body hoàn chỉnh
        Map<String, Object> requestBody = Map.of(
            "contents", Collections.singletonList(Map.of("parts", parts))
        );

        // Tạo HttpEntity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Gửi request POST
        String response = restTemplate.postForObject(apiUrl, entity, String.class);

        // Xử lý và trích xuất phần text từ response JSON của Google
        if (response != null) {
            JsonNode root = objectMapper.readTree(response);
            JsonNode textNode = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");
            
            String jsonResult = textNode.asText();
            // Xóa các ký tự không phải JSON mà Gemini có thể trả về
            return jsonResult.replace("```json", "").replace("```", "").trim();
        }

        throw new IOException("Không nhận được phản hồi hợp lệ từ Google AI API.");
    }
}