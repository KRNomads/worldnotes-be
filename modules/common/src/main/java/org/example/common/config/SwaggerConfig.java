package org.example.common.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("worldnote 서버 API")
                        .description("worldnote 서버 API")
                        .version("1.0.0"))
                .tags(List.of(
                        new Tag().name("OAuth2").description("OAuth2 소셜 로그인 관련 API")
                ))
                .paths(createOAuthPaths()); // 보안 설정 제거
    }

    private Paths createOAuthPaths() {
        Paths paths = new Paths();

        // 구글 로그인 경로 추가
        paths.addPathItem("/oauth2/authorization/google", new PathItem()
                .get(new io.swagger.v3.oas.models.Operation()
                        .summary("구글 로그인")
                        .description("구글 소셜 로그인 인증을 위한 경로")
                        .tags(List.of("OAuth2")) // 태그 추가
                        .parameters(List.of(
                                new io.swagger.v3.oas.models.parameters.Parameter()
                                        .in("query")
                                        .name("state")
                                        .description("OAuth2 인증 후 리디렉션할 URI 정보 (예: redirectUri=...)")
                                        .required(false)
                                        .example("redirectUri=https://www.example.com/login-success")
                                        .schema(new io.swagger.v3.oas.models.media.StringSchema())
                        ))
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
