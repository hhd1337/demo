package com.umc.hackathon_demo.domain.imagegen.service;


import com.umc.hackathon_demo.domain.imagegen.dto.ImageGenRequestDTO;
import com.umc.hackathon_demo.domain.imagegen.dto.ImageGenResponseDTO;

public interface ImageGenService {
    ImageGenResponseDTO generateImage(ImageGenRequestDTO request);
}
