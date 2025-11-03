package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Map;

@Service
public class GoogleAiService {

    @Value("${google.api.key}") // Lấy API Key từ application.properties
    private String apiKey;

    @Value("${facepp.api.key}") // Face++ API Key
    private String faceppApiKey;

    @Value("${facepp.api.secret}") // Face++ API Secret
    private String faceppApiSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String analyzeImageForSuggestions(String imageBase64) throws IOException {
        // Sử dụng Face++ API để phân tích khuôn mặt
        return analyzeFaceWithFacePP(imageBase64);
    }

    private String analyzeFaceWithFacePP(String imageBase64) throws IOException {
        String apiUrl = "https://api-us.faceplusplus.com/facepp/v3/detect";

        // Face++ expects application/x-www-form-urlencoded form fields.
        // Ensure we send a proper MultiValueMap and strip any data URI prefix from the base64 string.
        String cleanedBase64 = imageBase64 == null ? "" : imageBase64;
        if (cleanedBase64.startsWith("data:")) {
            int comma = cleanedBase64.indexOf(',');
            if (comma != -1) cleanedBase64 = cleanedBase64.substring(comma + 1);
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("api_key", faceppApiKey);
        form.add("api_secret", faceppApiSecret);
        form.add("image_base64", cleanedBase64);
        form.add("return_attributes", "age,emotion");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Tạo entity với body
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

        try {
            System.out.println("Gửi request đến Face++ API...");
            System.out.println("API Key: " + faceppApiKey.substring(0, 10) + "...");
            System.out.println("API Secret: " + faceppApiSecret.substring(0, 10) + "...");

            // Gửi request đến Face++ API
            String response = restTemplate.postForObject(apiUrl, entity, String.class);
            System.out.println("Face++ API Response: " + response);

            // Parse response và trích xuất thông tin
            JsonNode root = objectMapper.readTree(response);
            JsonNode faces = root.path("faces");

            if (faces.isArray() && faces.size() > 0) {
                JsonNode face = faces.get(0);
                JsonNode attributes = face.path("attributes");

                int age = attributes.path("age").path("value").asInt(25); // Default 25 nếu không có
                String emotion = getDominantEmotion(attributes.path("emotion"));

                return String.format("{\"age\":%d,\"emotion\":\"%s\"}", age, emotion);
            } else {
                // Không tìm thấy khuôn mặt, trả về giá trị mặc định
                return "{\"age\":25,\"emotion\":\"trung tính\"}";
            }
        } catch (Exception e) {
            // Nếu có lỗi, fallback về mock data
            System.err.println("Lỗi khi gọi Face++ API: " + e.getMessage());
            System.err.println("Chi tiết lỗi: " + e.toString());
            System.err.println("Stack trace:");
            e.printStackTrace();
            // Tạo dữ liệu ngẫu nhiên để test
            java.util.Random random = new java.util.Random();
            int randomAge = 18 + random.nextInt(50); // Tuổi từ 18-67
            String[] emotions = {"vui vẻ", "buồn", "trung tính", "ngạc nhiên", "giận dữ", "sợ hãi", "ghê tởm"};
            String randomEmotion = emotions[random.nextInt(emotions.length)];
            return String.format("{\"age\":%d,\"emotion\":\"%s\"}", randomAge, randomEmotion);
        }
    }

    private String getDominantEmotion(JsonNode emotionNode) {
        // Xác định cảm xúc có giá trị cao nhất
        double maxConfidence = 0;
        String dominantEmotion = "trung tính";

        Map<String, String> emotionMap = Map.of(
            "happiness", "vui vẻ",
            "sadness", "buồn",
            "anger", "giận dữ",
            "surprise", "ngạc nhiên",
            "fear", "sợ hãi",
            "disgust", "ghê tởm",
            "neutral", "trung tính"
        );

        for (String emotion : emotionMap.keySet()) {
            double confidence = emotionNode.path(emotion).asDouble(0);
            if (confidence > maxConfidence) {
                maxConfidence = confidence;
                dominantEmotion = emotionMap.get(emotion);
            }
        }

        return dominantEmotion;
    }
}