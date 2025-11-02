package com.pstripplanner.pstripplanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportationInfo {
    
    private String fromStartCity;
    private String toDestination;
    private String recommendedMode;
    private String estimatedCost;
    private String estimatedDuration;
}
