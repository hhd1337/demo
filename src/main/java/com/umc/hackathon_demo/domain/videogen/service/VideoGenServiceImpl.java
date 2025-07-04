//package com.umc.hackathon_demo.domain.videogen.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.umc.hackathon_demo.domain.videogen.dto.VideoGenRequestDTO;
//import com.umc.hackathon_demo.domain.videogen.dto.VideoGenResponseDTO;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.UUID;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class VideoGenServiceImpl implements VideoGenService {
//
//    private final S3Uploader s3Uploader; // S3 업로더는 사용자 프로젝트에 맞춰 주입하세요.
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
////    private static final String API_URL = "https://api.piapi.ai/api/v1/task";
////
////    @Value("${spring.ai.openai.api-key}")
////    private String API_KEY;
//
//    @Value("${spring.ai.piapi.video.api-key}")
//    private String API_KEY;
//
//    @Value("${spring.ai.piapi.video.base-url}")
//    private String API_URL;
//
//
//    //private static final String API_KEY = "YOUR_PIAPI_API_KEY"; // 환경 변수 처리 권장
//
//    @Override
//    public VideoGenResponseDTO.Response generateVideo(VideoGenRequestDTO.Request request) {
//        try {
//            // Step 1. PiAPI에 POST 요청
//            String taskId = sendVideoGenerationRequest(request.getPrompt(), request.getImage());
//
//            // Step 2. 생성 완료까지 polling
//            String videoUrl = waitForVideoCompletion(taskId);
//
//            // Step 3. 비디오를 다운로드 후 S3 업로드
////            File videoFile = downloadVideoFromUrl(videoUrl);
////            String s3Url = s3Uploader.upload(videoFile, "test"); // test 디렉토리에 업로드
//            String s3Url = "비디오를 다운로드 후 S3 업로드 하는 로직 구현 전임. 임시 url";
//            // Step 4. 반환
//            return new VideoGenResponseDTO.Response(videoUrl, s3Url);
//
//        } catch (Exception e) {
//            throw new RuntimeException("비디오 생성 중 오류 발생: " + e.getMessage(), e);
//        }
//    }
//
//    /*private String sendVideoGenerationRequest(String prompt, MultipartFile image) throws Exception {
//        // TODO: Multipart 요청 필요 시 이미지 업로드 후 URL 생성하여 함께 전송
//        // 여기선 텍스트 기반으로만 보냄 (i2v-01은 이미지 기반)
//        String payload = String.format("""
//            {
//                "model": "%s",
//                "service_mode": "public",
//                "prompt": "%s"
//            }
//        """, (image != null ? "i2v-01" : "t2v-01"), prompt.replace("\"", "\\\""));
//
//        String endpoint = API_URL + "/task";
//        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection(); // ✔️ 실제 endpoint로 요청
//
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("x-api-key", API_KEY);
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setDoOutput(true);
//        conn.getOutputStream().write(payload.getBytes());
//
//        JsonNode response = objectMapper.readTree(conn.getInputStream());
//        return response.get("data").get("task_id").asText();
//    }*/
//
//    private String sendVideoGenerationRequest(String prompt, MultipartFile image) throws Exception {
//        String model = (image != null) ? "i2v-01" : "t2v-01";
//
//        String payload = "";
//        if (image != null) {
//            // S3에 먼저 업로드한 URL이라고 가정
//            String imageUrl = s3Uploader.upload(image, "prompt-images"); // 이미지 먼저 업로드
//            payload = String.format("""
//            {
//                "model": "%s",
//                "service_mode": "public",
//                "prompt": "%s",
//                "image_url": "%s"
//            }
//        """, model, prompt.replace("\"", "\\\""), imageUrl);
//        } else {
//            payload = String.format("""
//            {
//                "model": "%s",
//                "service_mode": "public",
//                "prompt": "%s"
//            }
//        """, model, prompt.replace("\"", "\\\""));
//        }
//
//        URL url = new URL(API_URL + "/task");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("x-api-key", API_KEY);
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setDoOutput(true);
//        conn.getOutputStream().write(payload.getBytes());
//
//        if (conn.getResponseCode() >= 400) {
//            // 응답 바디를 읽어야 정확한 에러 메시지 확인 가능
//            InputStream errorStream = conn.getErrorStream();
//            String errorMessage = new String(errorStream.readAllBytes());
//            throw new RuntimeException("PiAPI 요청 실패: " + errorMessage);
//        }
//
//        JsonNode response = objectMapper.readTree(conn.getInputStream());
//        return response.get("data").get("task_id").asText();
//    }
//
//
//    private String waitForVideoCompletion(String taskId) throws Exception {
//        String statusUrl = "https://api.piapi.ai/api/v1/task/" + taskId;
//        while (true) {
//            HttpURLConnection conn = (HttpURLConnection) new URL(statusUrl).openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("x-api-key", API_KEY);
//            JsonNode response = objectMapper.readTree(conn.getInputStream());
//            String status = response.get("data").get("status").asText();
//            if ("completed".equals(status)) {
//                return response.get("data").get("output").get("video_url").asText();
//            } else if ("failed".equals(status)) {
//                throw new RuntimeException("비디오 생성 실패: " + response.toPrettyString());
//            }
//            Thread.sleep(3000);
//        }
//    }
//
//    private File downloadVideoFromUrl(String videoUrl) throws Exception {
//        URL url = new URL(videoUrl);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        InputStream inputStream = conn.getInputStream();
//        File tempFile = File.createTempFile("video-", ".mp4");
//        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                fos.write(buffer, 0, bytesRead);
//            }
//        }
//        return tempFile;
//    }
//}
