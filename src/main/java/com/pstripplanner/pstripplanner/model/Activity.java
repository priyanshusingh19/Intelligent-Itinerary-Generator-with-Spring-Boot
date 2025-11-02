package com.pstripplanner.pstripplanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    
    private String time;
    private String name;
    private String description;
    private String location;
    private String estimatedCost;
    private String duration;
}
