package com.pstripplanner.pstripplanner.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripRequest {
    
    @NotBlank(message = "Start city is required")
    private String startCity;
    
    @NotBlank(message = "Destination is required")
    private String destination;
    
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 day")
    private Integer duration;
    
    @NotBlank(message = "Budget is required")
    private String budget; // low, medium, high
    
    private List<String> interests;
    
    private String travelStyle; // adventure, cultural, relaxation, business, family
    
    private String startDate; // ISO format: YYYY-MM-DD
    
    @Min(value = 1, message = "Number of travelers must be at least 1")
    private Integer numberOfTravelers;
    
    private String additionalPreferences;
}
