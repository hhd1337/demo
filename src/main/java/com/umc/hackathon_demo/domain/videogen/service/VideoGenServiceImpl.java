package com.umc.hackathon_demo.domain.videogen.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.hackathon_demo.domain.s3.AmazonS3Manager;
import com.umc.hackathon_demo.domain.s3.UuidRepository;
import com.umc.hackathon_demo.domain.videogen.dto.VideoGenRequestDTO;
import com.umc.hackathon_demo.domain.videogen.dto.VideoGenResponseDTO;
import com.umc.hackathon_demo.entity.Uuid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoGenServiceImpl implements VideoGenService {

    private final AmazonS3Manager amazonS3Manager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${piapi.video.api-key}")
    private String apiKey;

    @Value("${piapi.video.base-url}")
    private String baseUrl;

    private final UuidRepository uuidRepository;

    @Override
    public VideoGenResponseDTO.Response generateVideo(VideoGenRequestDTO.Request request) {
        try {
            String taskId = sendVideoGenerationRequest(request.getPrompt());
            String videoUrl = waitForVideoCompletion(taskId);
            File downloadedVideo = downloadVideoFromUrl(videoUrl);
            String s3Key = amazonS3Manager.generateTestsKeyName(uuidRepository.save(Uuid.create())) + ".mp4";
            String s3Url = amazonS3Manager.uploadFile(s3Key, downloadedVideo);
            return new VideoGenResponseDTO.Response(videoUrl, s3Url);
        } catch (Exception e) {
            throw new RuntimeException("비디오 생성 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private String sendVideoGenerationRequest(String prompt) throws Exception {
        String payload = String.format("""
                {
                  "model": "kling",
                  "task_type": "video_generation",
                  "input": {
                    "prompt": "%s",
                    "cfg_scale": 0.5,
                    "duration": 5,
                    "aspect_ratio": "16:9",
                    "mode": "std"
                  },
                  "config": {
                    "service_mode": "public",
                    "webhook_config": {
                      "endpoint": "",
                      "secret": ""
                    }
                  }
                }
                """, prompt.replace("\"", "\\\""));

        log.info("[PIAPI-KLING] 요청 payload: {}", payload);

        URL url = new URL(baseUrl + "/task");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("x-api-key", apiKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));

        if (conn.getResponseCode() >= 400) {
            InputStream errorStream = conn.getErrorStream();
            String errorMessage = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
            throw new RuntimeException("PiAPI 요청 실패: " + errorMessage);
        }

        JsonNode response = objectMapper.readTree(conn.getInputStream());
        return response.get("data").get("task_id").asText();
    }

    private String waitForVideoCompletion(String taskId) throws Exception {
        String statusUrl = baseUrl + "/task/" + taskId;
        while (true) {
            HttpURLConnection conn = (HttpURLConnection) new URL(statusUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-api-key", apiKey);
            JsonNode response = objectMapper.readTree(conn.getInputStream());
            String status = response.get("data").get("status").asText();
            log.info("[PIAPI-KLING] Task Status: {}", status);
            if ("completed".equals(status)) {
                return response.get("data").get("output").get("video_url").asText();
            } else if ("failed".equals(status)) {
                throw new RuntimeException("비디오 생성 실패: " + response.toPrettyString());
            }
            Thread.sleep(3000);
        }
    }

    private File downloadVideoFromUrl(String videoUrl) throws Exception {
        URL url = new URL(videoUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inputStream = conn.getInputStream();
        File tempFile = File.createTempFile("video-", ".mp4");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }
}