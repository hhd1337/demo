package com.umc.hackathon_demo.domain.gpt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GPTServiceImpl implements GPTService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Override
    public String askGpt(String prompt) {
        // 1. 요청 본문 구성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");  // 최신 gpt-4o 모델
        requestBody.put("temperature", 0.7); // 응답 다양성 조절
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));

        // 2. 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey); // Authorization: Bearer {API_KEY}

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 3. 요청 및 응답 처리
            ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_API_URL, httpEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            return "GPT로부터 유효한 응답을 받지 못했습니다.";
        } catch (Exception e) {
            log.error("OpenAI GPT-4o API 호출 실패", e);
            return "GPT 요청 중 오류가 발생했습니다.";
        }
    }
}

