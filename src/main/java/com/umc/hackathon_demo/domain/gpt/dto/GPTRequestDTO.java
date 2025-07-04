package com.umc.hackathon_demo.domain.gpt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class GPTRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class PromptRequest {
        private String prompt;
    }
}

