package com.sparta.blog.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "블로그 API 명세서",
                description = "블로그의 CRUD 기능을 자세히 보여주는 API입니다.",
                version = "v3"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

//    @Bean
//    public GroupedOpenApi chatOpenApi() {
//        String[] paths = {"/v3/"};
//
//        return GroupedOpenApi.builder()
//                .group("채팅서비스 API v3")
//                .pathsToMatch(paths)
//                .build();
//    }
}
