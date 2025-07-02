package com.umc.hackathon_demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI Bean 등록
     * - API 기본 정보 설정 (제목, 설명, 버전)
     * - 서버 URL 설정
     * - JWT 인증을 위한 SecurityScheme 및 SecurityRequirement 설정
     */
    @Bean
    public OpenAPI TokkitAPI() {

        // API 문서 정보 설정
        Info info = new Info()
                .title("(앱 이름) API")                            // 문서 제목
                .description("_____팀 (앱 이름) API 명세서")      // 문서 설명
                .version("1.0.0");                              // 문서 버전

        // JWT 인증 방식 설정
        String jwtSchemeName = "JWT TOKEN"; // Security Scheme 이름 (임의 지정 가능)

        // API 요청 시 사용할 인증 방식 명시
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        // JWT 인증 방식 세부 정보 설정
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)      // HTTP 인증 방식 사용
                        .scheme("bearer")                    // Bearer 토큰 방식
                        .bearerFormat("JWT"));               // JWT 형식 명시

        // OpenAPI 객체 반환
        return new OpenAPI()
                .addServersItem(new Server().url("/"))       // 기본 서버 URL 설정 (현재 요청 기준 상대경로)
                .info(info)                                  // 문서 정보 등록
                .addSecurityItem(securityRequirement)        // 인증 방식 등록
                .components(components);                     // 인증 방식 세부 구성 등록
    }
}
