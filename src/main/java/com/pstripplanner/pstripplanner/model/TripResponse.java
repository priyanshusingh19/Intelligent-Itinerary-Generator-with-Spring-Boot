package com.pstripplanner.pstripplanner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripResponse {
    
    private String tripId;
    private String startCity;
    private String destination;
    private Integer duration;
    private List<DayItinerary> itinerary;
    private String estimatedBudget;
    private List<String> travelTips;
    private TransportationInfo transportation;
    private LocalDateTime generatedAt;
}
