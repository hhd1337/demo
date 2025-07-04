package com.umc.hackathon_demo.domain.ocr.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class ClovaOcrClient {

    @Value("${clova.ocr.secret}")
    private String ocrSecret;

    @Value("${clova.ocr.url}")
    private String ocrUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String extractText(MultipartFile image) {
        try {
            // 1. 파일 리소스 변환
            ByteArrayResource fileResource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };

            // 2. message JSON (문자열로 직렬화)
            String messageJson = objectMapper.writeValueAsString(
                    new Message(UUID.randomUUID().toString(), System.currentTimeMillis())
            );

            // 3. multipart/form-data 구성
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);
            body.add("message", messageJson);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("X-OCR-SECRET", ocrSecret);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 4. API 요청
            ResponseEntity<String> response = restTemplate.postForEntity(ocrUrl, requestEntity, String.class);

            // 5. 응답 파싱
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode fields = root.path("images").get(0).path("fields");

                return StreamSupport.stream(fields.spliterator(), false)
                        .map(field -> field.path("inferText").asText())
                        .collect(Collectors.joining(" "));
            } else {
                log.warn("CLOVA OCR 응답 실패: {}", response.getStatusCode());
                return "";
            }

        } catch (Exception e) {
            log.error("CLOVA OCR 호출 중 예외 발생", e);
            return "";
        }
    }

    // 내부 static class: message JSON 생성용
    private record Message(String requestId, long timestamp) {
        public String getVersion() {
            return "V2";
        }

        public String getLang() {
            return "ko";
        }

        public Object getImages() {
            return new Object[] { new Image("png", "uploaded") };
        }

        public boolean isEnableTableDetection() {
            return false;
        }

        private record Image(String format, String name) {}
    }
}
