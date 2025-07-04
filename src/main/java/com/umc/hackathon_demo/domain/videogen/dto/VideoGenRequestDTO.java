package com.umc.hackathon_demo.domain.videogen.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

public class VideoGenRequestDTO {

    @Getter
    @AllArgsConstructor
    public static class Request {

        @Schema(description = "비디오를 생성할 텍스트 내용", example = "A cat chasing a butterfly in a sunny field")
        private String prompt;

        @Schema(description = "비디오 생성을 위한 참조 이미지 (선택)", type = "string", format = "binary")
        private MultipartFile image;
    }
}
