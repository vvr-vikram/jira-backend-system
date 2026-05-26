package com.assignment.jira.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Jira-like Task Management API")
                        .version("1.0")
                        .description("API Documentation for Enterprise Backend Assignment - Jira Like Task Management System"));
    }
}
