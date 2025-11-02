package com.pstripplanner.pstripplanner.service;

import com.google.common.collect.ImmutableList;
import com.google.genai.Client;
import com.google.genai.types.*;
import com.pstripplanner.pstripplanner.config.AIConfig;
import com.pstripplanner.pstripplanner.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripPlannerService {
    
    private final Client geminiClient;
    private final AIConfig aiConfig;
    private final AIResponseParser responseParser;
    
    public TripResponse generateTripPlan(TripRequest request) {
        log.info("Generating trip plan for destination: {} from {}", 
                request.getDestination(), request.getStartCity());
        
        String prompt = buildPrompt(request);
        log.debug("Generated prompt: {}", prompt);
        
        int maxRetries = aiConfig.getMaxRetries();
        int attempt = 0;
        Exception lastException = null;
        
        while (attempt < maxRetries) {
            try {
                attempt++;
                log.debug("Attempt {} of {}", attempt, maxRetries);
                
                // Build content for Gemini API
                List<Content> contents = ImmutableList.of(
                    Content.builder()
                        .role("user")
                        .parts(ImmutableList.of(Part.fromText(prompt)))
                        .build()
                );
                
                // Configure generation
                GenerateContentConfig config = GenerateContentConfig.builder().build();
                
                // Call Gemini API
                GenerateContentResponse response = geminiClient.models.generateContent(
                    aiConfig.getModel(), 
                    contents, 
                    config
                );
                
                // Extract text from response
                String aiResponse = response.candidates().get().get(0)
                    .content().get()
                    .parts().get().get(0)
                    .text().get();
                    
                log.debug("AI Response received, length: {}", aiResponse.length());
                
                return responseParser.parseResponse(aiResponse, request);
                
            } catch (Exception e) {
                lastException = e;
                log.warn("Attempt {} failed: {}", attempt, e.getMessage());
                
                if (attempt < maxRetries) {
                    try {
                        // Exponential backoff: 1s, 2s, 4s
                        long waitTime = (long) Math.pow(2, attempt - 1) * 1000;
                        log.info("Retrying in {} ms...", waitTime);
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                }
            }
        }
        
        log.error("All {} attempts failed. Last error: {}", maxRetries, 
                lastException != null ? lastException.getMessage() : "Unknown");
        throw new RuntimeException("Failed to generate trip plan after " + maxRetries + 
                " attempts: " + (lastException != null ? lastException.getMessage() : "Unknown error"), 
                lastException);
    }
    
    private String buildPrompt(TripRequest request) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("You are an expert travel planner. Create a detailed, personalized trip itinerary based on the following requirements:\n\n");
        
        prompt.append("**Trip Details:**\n");
        prompt.append("- Starting from: ").append(request.getStartCity()).append("\n");
        prompt.append("- Destination: ").append(request.getDestination()).append("\n");
        prompt.append("- Duration: ").append(request.getDuration()).append(" days\n");
        prompt.append("- Budget: ").append(request.getBudget()).append("\n");
        prompt.append("- Number of travelers: ").append(request.getNumberOfTravelers()).append("\n");
        
        if (request.getStartDate() != null) {
            prompt.append("- Start date: ").append(request.getStartDate()).append("\n");
        }
        
        if (request.getTravelStyle() != null) {
            prompt.append("- Travel style: ").append(request.getTravelStyle()).append("\n");
        }
        
        if (request.getInterests() != null && !request.getInterests().isEmpty()) {
            prompt.append("- Interests: ").append(String.join(", ", request.getInterests())).append("\n");
        }
        
        if (request.getAdditionalPreferences() != null) {
            prompt.append("- Additional preferences: ").append(request.getAdditionalPreferences()).append("\n");
        }
        
        prompt.append("\n**Please provide:**\n");
        prompt.append("1. Transportation details from ").append(request.getStartCity())
              .append(" to ").append(request.getDestination()).append(" (mode, estimated cost, duration)\n");
        prompt.append("2. Day-by-day itinerary with:\n");
        prompt.append("   - Morning, afternoon, and evening activities\n");
        prompt.append("   - Specific locations and attractions\n");
        prompt.append("   - Estimated costs for each activity\n");
        prompt.append("   - Time allocations\n");
        prompt.append("   - Meal recommendations\n");
        prompt.append("3. Accommodation suggestions\n");
        prompt.append("4. Total estimated budget breakdown\n");
        prompt.append("5. Important travel tips and local customs\n");
        prompt.append("6. Best times to visit attractions to avoid crowds\n\n");
        
        prompt.append("Format the response in a clear, structured way with sections for each day and category.");
        
        return prompt.toString();
    }
}
