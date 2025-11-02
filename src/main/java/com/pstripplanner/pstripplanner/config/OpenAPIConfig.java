package com.pstripplanner.pstripplanner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PS Trip Planner API")
                        .version("1.0")
                        .description("AI-powered trip planning REST API")
                        .contact(new Contact()
                                .name("PS Trip Planner Team")
                                .email("support@pstripplanner.com")));
    }
}
