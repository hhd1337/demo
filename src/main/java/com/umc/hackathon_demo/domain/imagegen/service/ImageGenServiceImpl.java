package com.umc.hackathon_demo.domain.imagegen.service;


import com.umc.hackathon_demo.domain.imagegen.client.DallEClient;
import com.umc.hackathon_demo.domain.imagegen.dto.ImageGenRequestDTO;
import com.umc.hackathon_demo.domain.imagegen.dto.ImageGenResponseDTO;
import com.umc.hackathon_demo.domain.imagegen.util.GptSummarizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageGenServiceImpl implements ImageGenService {

    private final DallEClient dallEClient;
    //private final GptSummarizer gptSummarizer;

    @Override
    public ImageGenResponseDTO generateImage(ImageGenRequestDTO request) {
        // 1. request body에서 content 추출
        String content = request.getContent();

        // 2. content가 DALL·E_3의 프롬프트 글자 수 limit(1000자) 초과 시 800자 이내로 줄임
        // 방법 1. 앞부분 800자 자름
        if (content.length() > 800) {
            content = content.substring(0, 800);
        }
        // 방법 2. gpt api를 통해 요약
        /*if (content.length() > 800) {
            content = gptSummarizer.summarize(content);
        }*/

        // 3. 프롬프트 제작
        String prompt = String.format(
                "You are a highly skilled image creator with over 30 years of experience. Please create a detailed and visually compelling image that illustrates the following description:\n\"%s\"\nStyle: %s. Do not include any text or captions in the image. Do not use diagrams, labels, arrows, or illustration styles.",
                content,
                request.getStyle() != null && !request.getStyle().isBlank()
                        ? request.getStyle()
                        : "realistic"
        );

        // 4. 프롬프트 확인용 로그
        log.info("DALL·E 전달용 프롬프트 : \n{}", prompt);

        // 5. 이미지 생성
        String imageUrl = dallEClient.createImage(prompt, "dall-e-3");

        // 6. URL을 응답으로 전송
        return new ImageGenResponseDTO(imageUrl);
    }
}
