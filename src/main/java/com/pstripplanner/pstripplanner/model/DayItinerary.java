package com.pstripplanner.pstripplanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayItinerary {
    
    private Integer day;
    private String title;
    private List<Activity> activities;
    private List<String> recommendations;
    private String meals;
    private String accommodation;
}
