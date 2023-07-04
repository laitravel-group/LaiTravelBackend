package com.laitravel.laitravelbe.place;

import com.laitravel.laitravelbe.model.Place;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlaceController {
    final PlaceService placeService;
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/places")
    public List<Place> getPlaces(@RequestParam(value="city") String city, @RequestParam(value="startDate") String startDate, @RequestParam(value="endDate") String endDate) {
        return placeService.placeSearch(city, startDate, endDate);
    }



}
