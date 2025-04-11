package org.bodyguide_sv.common.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components().addSecuritySchemes("JWT", createAPIKeyScheme()))
                .info(new Info()
                        .title("worldnote 서버 API")
                        .description("worldnote 서버 API")
                        .version("1.0.0"))
                .tags(List.of(
                        new Tag().name("OAuth2").description("OAuth2 소셜 로그인 관련 API")
                )) // 태그 정의
                .paths(createOAuthPaths());

        return openAPI;
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    private Paths createOAuthPaths() {
        Paths paths = new Paths();

        // 구글 로그인 경로 추가
        paths.addPathItem("/oauth2/authorization/google", new PathItem()
                .get(new io.swagger.v3.oas.models.Operation()
                        .summary("구글 로그인")
                        .description("구글 소셜 로그인 인증을 위한 경로")
                        .tags(List.of("OAuth2")) // 태그 추가
                        .responses(createOAuthApiResponses())));

        return paths;
    }

    private ApiResponses createOAuthApiResponses() {
        ApiResponses responses = new ApiResponses();
        responses.addApiResponse("302", new ApiResponse().description("리다이렉션 성공"));
        responses.addApiResponse("400", new ApiResponse().description("잘못된 요청"));
        responses.addApiResponse("500", new ApiResponse().description("서버 오류"));
        return responses;
    }

}
