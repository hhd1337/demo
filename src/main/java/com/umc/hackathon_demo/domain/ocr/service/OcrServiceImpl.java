package com.umc.hackathon_demo.domain.ocr.service;

import com.umc.hackathon_demo.domain.ocr.client.ClovaOcrClient;
import com.umc.hackathon_demo.domain.ocr.dto.OcrResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class OcrServiceImpl implements OcrService {

    private final ClovaOcrClient clovaOcrClient; // CLOVA OCR API를 호출하는 클라이언트
    //private final GptTranslator gptTranslator; // 한글 텍스트를 영어로 번역해주는 GPT 번역기

    @Override
    public OcrResponseDTO processImage(MultipartFile image) {
        String rawText = clovaOcrClient.extractText(image); // 1. CLOVA OCR을 이용해 이미지에서 텍스트 추출
//        // 2. 텍스트에 한글이 포함되어 있다면 GPT를 통해 영어로 번역
//        String translated = KoreanTextUtil.containsKorean(rawText)
//                ? gptTranslator.translateToEnglish(rawText)
//                : rawText;
//        // 3. 영어 텍스트를 담은 DTO로 응답
//        return new OcrResponseDTO(translated);
        return new OcrResponseDTO(rawText);
    }
}

