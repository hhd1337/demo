package com.umc.hackathon_demo.domain.videogen;

import com.umc.hackathon_demo.common.exception.code.status.SuccessStatus;
import com.umc.hackathon_demo.common.response.ApiResponse;
import com.umc.hackathon_demo.domain.videogen.dto.VideoGenRequestDTO;
import com.umc.hackathon_demo.domain.videogen.dto.VideoGenResponseDTO;
import com.umc.hackathon_demo.domain.videogen.service.VideoGenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/video-gen")
@RequiredArgsConstructor
@Tag(name = "VideoGen", description = "텍스트 및 이미지 기반 비디오 생성 API")
public class VideoGenController {

    private final VideoGenService videoGenService;

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(
            summary = "비디오 생성 요청",
            description = "텍스트만으로 또는 텍스트 + 이미지로 영상을 생성하고, Hailuo API 결과 및 S3 업로드 URL을 반환합니다."
    )
    public ApiResponse<VideoGenResponseDTO.Response> generateVideo(
            @RequestPart("prompt") String prompt,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        VideoGenRequestDTO.Request requestDTO = new VideoGenRequestDTO.Request(prompt, image);
        return ApiResponse.of(
                SuccessStatus._OK,
                videoGenService.generateVideo(requestDTO)
        );
    }

}

