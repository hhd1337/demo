package com.umc.hackathon_demo.domain.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.hackathon_demo.config.AmazonConfig;
import com.umc.hackathon_demo.entity.Uuid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager{

    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    private final UuidRepository uuidRepository;

    public String uploadFile(String keyName, MultipartFile file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType()); // 중요
            metadata.setContentDisposition("inline"); // 다운로드 방지

            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), metadata));

            return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
        } catch (IOException e) {
            throw new RuntimeException("S3 파일 업로드 실패", e);
        }
    }

    public String uploadFile(String keyName, File file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.length());
        metadata.setContentType("video/mp4"); // mp4로 고정 (또는 동적 처리 가능)
        metadata.setContentDisposition("inline");

        amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), keyName, file));
        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }


    public String generateTestsKeyName(Uuid uuid) {
        return amazonConfig.getTestsPath() + '/' + uuid.getUuid();
    }
}
