package com.umc.hackathon_demo.domain.imagegen.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageGenResponseDTO {

    @Schema(description = "생성된 이미지 URL", example = "https://openai.com/image123.png")
    private String imageUrl;
}
