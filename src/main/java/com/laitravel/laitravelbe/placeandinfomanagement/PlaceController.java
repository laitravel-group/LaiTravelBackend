package com.laitravel.laitravelbe.placeandinfomanagement;

import com.google.maps.model.PlacesSearchResult;
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
        List<Place>results = placeService.placeSearch(city,startDate,endDate);
        System.out.println(results.size());
        return results;

    }



}
