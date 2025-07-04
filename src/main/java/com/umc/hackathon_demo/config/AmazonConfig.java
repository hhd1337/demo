package com.umc.hackathon_demo.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AmazonConfig {


    private AWSCredentials awsCredentials; // AWS 인증 객체를 저장하는 필드 (Spring Bean 등록 전 @PostConstruct에서 초기화)


    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey; // AWS IAM Access Key (application.yml에서 가져옴)


    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey; // AWS IAM Secret Key (application.yml에서 가져옴)


    @Value("${cloud.aws.region.static}")
    private String region; // AWS S3가 위치한 리전 (예: ap-northeast-2 = 서울)


    @Value("${cloud.aws.s3.bucket}")
    private String bucket; // 사용할 AWS S3 버킷 이름 (application.yml에서 지정)


    @Value("tests")
    private String testsPath; // S3 내 테스트 관련 파일 저장 디렉토리 경로 (폴더명: tests)

    //!! s3에 어떤 디렉토리를 만들고, 그안에 뭘 저장하고 싶다면!!
    //1. aws 콘솔에서 s3 디렉토리 생성
    //2. AmazonConfig에 private String ~~Path 변수 생성
    //3. AmazonS3Manager에 generate~~KeyName() 메서드 추가
    //4. 서비스 계층에서 사용.

    /**
     * AWS 인증 정보를 설정하는 초기화 메서드.
     * Spring이 이 클래스를 생성한 후 자동으로 호출되며,
     * accessKey, secretKey를 바탕으로 AWSCredentials를 초기화함.
     */
    @PostConstruct
    public void init() {
        this.awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    }

    /**
     * AmazonS3 클라이언트를 빈으로 등록하는 메서드.
     * 인증정보와 리전을 기반으로 S3와 통신 가능한 객체를 생성하여 반환함.
     * 주로 파일 업로드/다운로드 등의 기능을 수행할 때 사용됨.
     */
    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    /**
     * 인증 정보를 스프링 컨테이너에 빈으로 등록하는 메서드.
     * 다른 AWS 서비스에서 이 인증 정보를 참조하여 사용할 수 있도록 함.
     */
    @Bean
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new AWSStaticCredentialsProvider(awsCredentials);
    }
}
