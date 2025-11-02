package com.pstripplanner.pstripplanner.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Configuration
@Getter
public class AIConfig {
    
    @Value("${ai.api.key}")
    private String apiKey;
    
    @Value("${ai.model:gemini-2.0-flash-exp}")
    private String model;
    
    @Value("${ai.timeout:60}")
    private int timeout;
    
    @Value("${ai.max.retries:3}")
    private int maxRetries;
    
    @Bean
    public Client geminiClient() {
        return Client.builder()
                .apiKey(apiKey)
                .build();
    }
}
