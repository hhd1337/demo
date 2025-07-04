package com.umc.hackathon_demo.domain.ocr.util;

public class KoreanTextUtil {
    public static boolean containsKorean(String text) {
        return text != null && text.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
    }
}

