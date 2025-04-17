package com.minewaku.trilog.config;

import java.util.Arrays;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;


@Configuration
public class SwaggerConfig {

    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/api/**")
                .build();
    }

    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Trilog API")
                    .description("Trilog API Reference for Developers")
                    .version("1.0")
                    .license(new License().name("Trilog API License").url("http://minewaku.com"))
                    .contact(new Contact().name("minewaku").email("minewaku@gmail.com").url("http://minewaku.com"))
            )
            .components(new Components().addSecuritySchemes("bearer-jwt",
                new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")
                                    .in(SecurityScheme.In.HEADER)
                                    .name("Authorization")
            ))
            .addSecurityItem(new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
    }
}
