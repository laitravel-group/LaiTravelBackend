package com.laitravel.laitravelbe.TravelPlanBuilder;

import com.laitravel.laitravelbe.model.TripPlan;
import com.laitravel.laitravelbe.model.TripPlanDetailsPerDay;
import org.springframework.stereotype.Service;

@Service
public class TravelPlanService {

    public TripPlan tripPlan;

    public void addTravelPlan (TripPlanDetailsPerDay tripPlanDetailsPerDay){
        tripPlan.details().add(tripPlanDetailsPerDay);
    }

}
