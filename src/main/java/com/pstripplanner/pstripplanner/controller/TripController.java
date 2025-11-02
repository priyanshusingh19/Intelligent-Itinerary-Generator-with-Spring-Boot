package com.pstripplanner.pstripplanner.controller;

import com.pstripplanner.pstripplanner.model.TripRequest;
import com.pstripplanner.pstripplanner.model.TripResponse;
import com.pstripplanner.pstripplanner.service.TripPlannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Trip Planning", description = "AI-powered trip planning endpoints")
public class TripController {
    
    private final TripPlannerService tripPlannerService;
    
    @PostMapping("/generate")
    @Operation(summary = "Generate trip itinerary", 
               description = "Generate a personalized trip itinerary based on user preferences using AI")
    public ResponseEntity<TripResponse> generateTrip(@Valid @RequestBody TripRequest request) {
        log.info("Received trip generation request for: {} from {}", 
                request.getDestination(), request.getStartCity());
        
        try {
            TripResponse response = tripPlannerService.generateTripPlan(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error generating trip: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the trip planning service is running")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Trip Planner Service is running");
    }
}
