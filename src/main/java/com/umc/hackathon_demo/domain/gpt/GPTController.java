package com.umc.hackathon_demo.domain.gpt;

import com.umc.hackathon_demo.domain.gpt.dto.GPTRequestDTO;
import com.umc.hackathon_demo.domain.gpt.dto.GPTResponseDTO;
import com.umc.hackathon_demo.domain.gpt.service.GPTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "GPT", description = "GPT 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gpt")
public class GPTController {

    private final GPTService gptService;

    @Operation(
            summary = "GPT 응답 요청",
            description = "프롬프트를 GPT-4o에게 전달하고 응답을 받아옵니다."
    )
    @PostMapping("/ask")
    public ResponseEntity<GPTResponseDTO.PromptResponse> askGpt(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "GPT에게 전달할 프롬프트를 입력하세요",
                    required = true,
                    content = @Content(schema = @Schema(implementation = GPTRequestDTO.PromptRequest.class))
            )
            @RequestBody GPTRequestDTO.PromptRequest request
    ) {
        String answer = gptService.askGpt(request.getPrompt());
        return ResponseEntity.ok(new GPTResponseDTO.PromptResponse(answer));
    }
}

