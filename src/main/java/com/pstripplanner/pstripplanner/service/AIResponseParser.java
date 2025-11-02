package com.pstripplanner.pstripplanner.service;

import com.pstripplanner.pstripplanner.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class AIResponseParser {
    
    public TripResponse parseResponse(String aiResponse, TripRequest request) {
        log.debug("Parsing AI response");
        
        TripResponse response = TripResponse.builder()
                .tripId("trip_" + UUID.randomUUID().toString().substring(0, 8))
                .startCity(request.getStartCity())
                .destination(request.getDestination())
                .duration(request.getDuration())
                .generatedAt(LocalDateTime.now())
                .build();
        
        // Parse transportation info
        response.setTransportation(parseTransportation(aiResponse, request));
        
        // Parse daily itinerary
        response.setItinerary(parseDailyItinerary(aiResponse, request.getDuration()));
        
        // Parse budget
        response.setEstimatedBudget(parseBudget(aiResponse, request.getBudget()));
        
        // Parse travel tips
        response.setTravelTips(parseTravelTips(aiResponse));
        
        return response;
    }
    
    private TransportationInfo parseTransportation(String response, TripRequest request) {
        TransportationInfo info = new TransportationInfo();
        info.setFromStartCity(request.getStartCity());
        info.setToDestination(request.getDestination());
        
        // Try to extract transportation details from the response
        String lowerResponse = response.toLowerCase();
        
        if (lowerResponse.contains("flight") || lowerResponse.contains("plane")) {
            info.setRecommendedMode("Flight");
        } else if (lowerResponse.contains("train")) {
            info.setRecommendedMode("Train");
        } else if (lowerResponse.contains("bus")) {
            info.setRecommendedMode("Bus");
        } else if (lowerResponse.contains("car") || lowerResponse.contains("drive")) {
            info.setRecommendedMode("Car");
        } else {
            info.setRecommendedMode("Various options available");
        }
        
        info.setEstimatedCost("See detailed breakdown");
        info.setEstimatedDuration("Varies by mode");
        
        return info;
    }
    
    private List<DayItinerary> parseDailyItinerary(String response, int duration) {
        List<DayItinerary> itinerary = new ArrayList<>();
        
        // Split response into sections for each day
        String[] lines = response.split("\n");
        DayItinerary currentDay = null;
        int dayCounter = 0;
        
        for (String line : lines) {
            line = line.trim();
            
            // Check if this line indicates a new day
            if (line.matches("(?i).*day\\s*\\d+.*") || 
                line.matches("(?i).*\\d+\\s*day.*")) {
                
                if (currentDay != null) {
                    itinerary.add(currentDay);
                }
                
                dayCounter++;
                currentDay = new DayItinerary();
                currentDay.setDay(dayCounter);
                currentDay.setTitle("Day " + dayCounter);
                currentDay.setActivities(new ArrayList<>());
                currentDay.setRecommendations(new ArrayList<>());
            }
            
            // Add content to current day
            if (currentDay != null && !line.isEmpty()) {
                // This is simplified - in production, you'd want more sophisticated parsing
                if (line.length() > 10) {
                    currentDay.getRecommendations().add(line);
                }
            }
        }
        
        // Add the last day
        if (currentDay != null) {
            itinerary.add(currentDay);
        }
        
        // If parsing didn't work well, create basic structure
        if (itinerary.isEmpty()) {
            for (int i = 1; i <= duration; i++) {
                DayItinerary day = new DayItinerary();
                day.setDay(i);
                day.setTitle("Day " + i);
                day.setActivities(new ArrayList<>());
                day.setRecommendations(Arrays.asList(
                    "Detailed itinerary provided by AI - see full response"
                ));
                itinerary.add(day);
            }
        }
        
        return itinerary;
    }
    
    private String parseBudget(String response, String budgetLevel) {
        // Try to find budget mentions in the response
        Pattern pattern = Pattern.compile("\\$\\d+[,\\d]*|\\d+[,\\d]*\\s*(?:USD|EUR|GBP|dollars?|euros?|pounds?)");
        Matcher matcher = pattern.matcher(response);
        
        List<String> budgets = new ArrayList<>();
        while (matcher.find() && budgets.size() < 3) {
            budgets.add(matcher.group());
        }
        
        if (!budgets.isEmpty()) {
            return "Estimated: " + String.join(", ", budgets) + " (based on " + budgetLevel + " budget)";
        }
        
        return "Budget aligned with " + budgetLevel + " preference - see detailed breakdown";
    }
    
    private List<String> parseTravelTips(String response) {
        List<String> tips = new ArrayList<>();
        
        String[] lines = response.split("\n");
        boolean inTipsSection = false;
        
        for (String line : lines) {
            line = line.trim();
            
            if (line.toLowerCase().contains("tip") || 
                line.toLowerCase().contains("advice") ||
                line.toLowerCase().contains("recommendation")) {
                inTipsSection = true;
                continue;
            }
            
            if (inTipsSection && !line.isEmpty() && line.length() > 15) {
                tips.add(line.replaceAll("^[-*•]\\s*", ""));
                if (tips.size() >= 5) break;
            }
        }
        
        if (tips.isEmpty()) {
            tips.add("Check local weather conditions before traveling");
            tips.add("Book accommodations and major attractions in advance");
            tips.add("Keep copies of important documents");
        }
        
        return tips;
    }
}
