package com.pstripplanner.pstripplanner.service;

import com.pstripplanner.pstripplanner.model.TripRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TripPlannerServiceTest {

    @Test
    void testTripRequestValidation() {
        TripRequest request = new TripRequest();
        request.setStartCity("London, UK");
        request.setDestination("Paris, France");
        request.setDuration(5);
        request.setBudget("medium");
        request.setInterests(Arrays.asList("museums", "food"));
        request.setNumberOfTravelers(2);
        
        assertNotNull(request.getStartCity());
        assertNotNull(request.getDestination());
        assertEquals(5, request.getDuration());
        assertEquals("medium", request.getBudget());
    }
}
