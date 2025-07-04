package com.umc.hackathon_demo.domain.imagegen.controller;

import com.umc.hackathon_demo.common.exception.code.status.SuccessStatus;
import com.umc.hackathon_demo.common.response.ApiResponse;
import com.umc.hackathon_demo.domain.imagegen.dto.ImageGenRequestDTO;
import com.umc.hackathon_demo.domain.imagegen.dto.ImageGenResponseDTO;
import com.umc.hackathon_demo.domain.imagegen.service.ImageGenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image-gen")
@RequiredArgsConstructor
@Tag(name = "DALL·E", description = "텍스트 기반 이미지 생성 API")
public class ImageGenController {

    private final ImageGenService imageGenService;

    @PostMapping
    @Operation(summary = "텍스트를 기반으로 이미지 생성", description = "DALL·E 3 모델을 이용해 텍스트를 기반으로 어울리는 이미지를 생성합니다.")
    public ApiResponse<ImageGenResponseDTO> generateImage(@RequestBody @Valid ImageGenRequestDTO request) {
        return ApiResponse.of(
                SuccessStatus._OK,
                imageGenService.generateImage(request)
        );
    }
}
