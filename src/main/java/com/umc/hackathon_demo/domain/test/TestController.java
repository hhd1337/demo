package com.umc.hackathon_demo.domain.test;

import com.umc.hackathon_demo.common.response.ApiResponse;
import com.umc.hackathon_demo.domain.s3.AmazonS3Manager;
import com.umc.hackathon_demo.domain.s3.UuidRepository;
import com.umc.hackathon_demo.domain.test.dto.TestResponse;
import com.umc.hackathon_demo.domain.test.service.TestCommandService;
import com.umc.hackathon_demo.domain.test.service.TestQueryService;
import com.umc.hackathon_demo.entity.Uuid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Test", description = "테스트용 API")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestQueryService testQueryService;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;

    @GetMapping("")
    public ApiResponse<TestResponse.TestDTO> test() {
        return ApiResponse.onSuccess(TestConverter.toTempTestDTO());
    }

    @GetMapping("/exception")
    @Operation(
            summary = "에러 핸들링 테스트",
            description = "Query String으로 전달된 flag 값이 1일 경우 예외를 발생시키는 테스트용 API입니다."
    )
    public ApiResponse<TestResponse.ExceptionDTO> exceptionAPI(@RequestParam Integer flag){

        testQueryService.CheckFlag(flag);
        return ApiResponse.onSuccess(TestConverter.toExceptionDTO(flag));
    }

//    @PostMapping("/image")
//    @Operation(
//            summary = "테스트용 이미지 업로드",
//            description = "MultipartFile로 전달된 이미지를 S3의 'tests/' 디렉토리에 업로드하고, 해당 URL을 반환합니다."
//    )
//    public ApiResponse<TestResponse.ImageUploadResultDTO> uploadTestImage(
//            @RequestParam("image") MultipartFile image) {
//
//        // UUID 생성 및 저장
//        String uuid = UUID.randomUUID().toString();
//        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());
//
//        // KeyName 생성 및 S3 업로드
//        String url = s3Manager.uploadFile(s3Manager.generateTestsKeyName(savedUuid), image);
//
//        // 결과 DTO 반환
//        return ApiResponse.onSuccess(new TestResponse.ImageUploadResultDTO(url));
//    }


    private final TestCommandService testCommandService;

    @Operation(summary = "테스트용 이미지 업로드", description = "MultipartFile로 전달된 이미지를 S3의 tests 디렉토리에 업로드하고 URL을 반환합니다.")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadImage(
            @Parameter(description = "업로드할 이미지 파일", required = true)
            @RequestPart("image") MultipartFile image) {

        String url = testCommandService.uploadTestImage(image);
        return ApiResponse.onSuccess(url);
    }
}
