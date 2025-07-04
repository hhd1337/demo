package com.umc.hackathon_demo.domain.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class GPTResponseDTO {

    @Getter
    @AllArgsConstructor
    public static class PromptResponse {
        private String answer;
    }
}

