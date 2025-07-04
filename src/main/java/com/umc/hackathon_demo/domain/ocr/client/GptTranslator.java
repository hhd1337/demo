//package com.umc.hackathon_demo.domain.ocr.client;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class GptTranslator {
//
//    @Value("${spring.ai.openai.api-key}")
//    private String openAiApiKey;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public String translateToEnglish(String koreanText) {
//        try {
//            String apiUrl = "https://api.openai.com/v1/chat/completions";
//
//            // GPT 프롬프트 구성
//            Map<String, Object> systemMessage = Map.of(
//                    "role", "system",
//                    "content", "You are a translator that converts Korean into natural English for educational purposes."
//            );
//
//            Map<String, Object> userMessage = Map.of(
//                    "role", "user",
//                    "content", "Translate this into natural English:\n" + koreanText
//            );
//
//            Map<String, Object> requestBody = new HashMap<>();
//            requestBody.put("model", "gpt-3.5-turbo");
//            requestBody.put("messages", new Object[]{ systemMessage, userMessage });
//            requestBody.put("temperature", 0.7);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.setBearerAuth(openAiApiKey);
//
//            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
//
//            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                JsonNode json = objectMapper.readTree(response.getBody());
//                return json.path("choices").get(0).path("message").path("content").asText().trim();
//            } else {
//                log.warn("GPT 번역 실패: {}", response.getStatusCode());
//                return "[번역 실패]";
//            }
//        } catch (Exception e) {
//            log.error("GPT 번역 예외 발생", e);
//            return "[번역 예외]";
//        }
//    }
//}
//
