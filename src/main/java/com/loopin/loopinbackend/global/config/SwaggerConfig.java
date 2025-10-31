package com.loopin.loopinbackend.global.config;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenApiCustomizer globalResponsesCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    operation.getResponses().addApiResponse("401", new io.swagger.v3.oas.models.responses.ApiResponse()
                            .description("인증 실패")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(new Schema<>().$ref("#/components/schemas/ApiErrorResponse")))));
                    operation.getResponses().addApiResponse("403", new io.swagger.v3.oas.models.responses.ApiResponse()
                            .description("권한 없음")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(new Schema<>().$ref("#/components/schemas/ApiErrorResponse")))));
                }));
    }

}
