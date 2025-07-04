package com.umc.hackathon_demo.domain.ocr.service;

import com.umc.hackathon_demo.domain.ocr.dto.OcrResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface OcrService {
    OcrResponseDTO processImage(MultipartFile image);
}
