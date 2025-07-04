package com.umc.hackathon_demo.domain.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TestResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestDTO{
        String testString;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExceptionDTO{
        Integer flag;
    }

    @Getter
    @AllArgsConstructor
    public static class ImageUploadResultDTO {
        private String imageUrl;
    }

}
