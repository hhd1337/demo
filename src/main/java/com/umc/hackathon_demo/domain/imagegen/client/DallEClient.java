package com.umc.hackathon_demo.domain.imagegen.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DallEClient {

    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String createImage(String prompt, String model) {
        try {
            String url = "https://api.openai.com/v1/images/generations";

            Map<String, Object> requestBody = Map.of(
                    "prompt", prompt,
                    "n", 1,
                    "size", "1024x1024",  // 달리3은 1024x1024 밖에 지원하지 않음.
                    "model", model // 서비스에서 명시한 모델 사용
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("data").get(0).path("url").asText();
            }

            throw new RuntimeException("DALL·E API 호출 실패");

        } catch (Exception e) {
            log.error("DALL·E 이미지 생성 실패", e);
            throw new RuntimeException("이미지 생성 중 오류가 발생했습니다.", e);
        }
    }
}

