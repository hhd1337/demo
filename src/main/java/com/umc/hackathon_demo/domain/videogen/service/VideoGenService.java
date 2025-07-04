package com.umc.hackathon_demo.domain.videogen.service;

import com.umc.hackathon_demo.domain.videogen.dto.VideoGenRequestDTO;
import com.umc.hackathon_demo.domain.videogen.dto.VideoGenResponseDTO;

public interface VideoGenService {
    VideoGenResponseDTO.Response generateVideo(VideoGenRequestDTO.Request request);
}

