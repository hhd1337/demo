package com.umc.hackathon_demo.domain.test;

import com.umc.hackathon_demo.common.response.ApiResponse;
import com.umc.hackathon_demo.domain.test.dto.TestResponse;
import com.umc.hackathon_demo.domain.test.service.TestQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test", description = "테스트용 API")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestQueryService testQueryService;

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
}
