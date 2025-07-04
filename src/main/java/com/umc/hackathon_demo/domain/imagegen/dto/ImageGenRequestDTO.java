package com.umc.hackathon_demo.domain.imagegen.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ImageGenRequestDTO {

    @Schema(description = "이미지를 생성할 텍스트 내용", example = "The water cycle describes how water moves through the Earth's atmosphere.")
    @NotBlank
    private String content;

    @Schema(description = "이미지 스타일 (선택 사항)", example = "minimalist, digital art, watercolor")
    private String style; // optional
}
