package com.umc.hackathon_demo.domain.test.service;

import org.springframework.web.multipart.MultipartFile;

public interface TestCommandService {
    String uploadTestImage(MultipartFile image);
}

