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
        // TODO: Tích hợp Google AI API để phân tích ảnh thực tế
        // Hiện tại sử dụng mock data để demo
        return "{\"age\":25,\"emotion\":\"vui vẻ\"}";
    }
}