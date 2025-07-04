package com.umc.hackathon_demo.domain.ocr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class OcrResponseDTO {
    private String translatedText;
}