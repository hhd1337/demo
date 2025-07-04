package com.umc.hackathon_demo.domain.imagegen.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GptSummarizer {

    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String summarize(String content) {
        String prompt = """
        Summarize the following educational content into a single concise English paragraph within 400 characters. 
        Focus on the core concepts only. Do not include any formatting or bullet points. 
        Your response must not exceed 800 characters. The shorter and more focused the summary is, the better.
        Content:
        \"%s\"
        """.formatted(content);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            Map<String, Object> messages = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", new Object[] {
                            Map.of("role", "user", "content", prompt)
                    }
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(messages, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.openai.com/v1/chat/completions",
                    HttpMethod.POST,
                    request,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();

        } catch (Exception e) {
            throw new RuntimeException("GPT 요약 중 오류 발생", e);
        }
    }
}
