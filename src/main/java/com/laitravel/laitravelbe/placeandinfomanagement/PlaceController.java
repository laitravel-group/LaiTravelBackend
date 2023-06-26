package com.laitravel.laitravelbe.placeandinfomanagement;

import com.google.maps.model.PlacesSearchResult;
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

    @GetMapping("/getPlaces")
    public List<PlacesSearchResult> getPlaces(@RequestParam(value="city")String city, @RequestParam(value="start") String startDate, @RequestParam(value="end") String endDate) {
        List<PlacesSearchResult> results = placeService.placeSearch(city,startDate,endDate);
        return results;

    }





}
