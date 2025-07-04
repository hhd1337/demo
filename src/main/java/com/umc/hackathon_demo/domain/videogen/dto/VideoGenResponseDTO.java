package com.umc.hackathon_demo.domain.videogen.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class VideoGenResponseDTO {

    @Getter
    @AllArgsConstructor
    public static class Response {

        @Schema(description = "Hailuo API가 반환한 영상 URL", example = "https://piapi.ai/output/video123.mp4")
        private String hailuoVideoUrl;

        @Schema(description = "S3에 저장된 영상의 URL", example = "https://s3.amazonaws.com/your-bucket/test/video123.mp4")
        private String s3VideoUrl;
    }
}
