package com.umc.hackathon_demo.domain.test;


import com.umc.hackathon_demo.domain.test.dto.TestResponse;

public class TestConverter {
    public static TestResponse.TestDTO toTempTestDTO(){
        return TestResponse.TestDTO.builder()
                .testString("테스트 성공")
                .build();
    }

    public static TestResponse.ExceptionDTO toExceptionDTO(Integer flag){
        return TestResponse.ExceptionDTO.builder()
                .flag(flag)
                .build();
    }
}
