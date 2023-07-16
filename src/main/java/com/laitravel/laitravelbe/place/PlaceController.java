package com.laitravel.laitravelbe.place;

import com.laitravel.laitravelbe.api.GoogleCloudService;
import com.laitravel.laitravelbe.db.CityRepository;
import com.laitravel.laitravelbe.model.Place;
import com.laitravel.laitravelbe.model.response.PlacesResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlaceController {
    final PlaceService placeService;
    final GoogleCloudService gcsService;
    final String bucketName;
    final CityRepository cityRepository;
    public PlaceController(PlaceService placeService, GoogleCloudService gcsService, @Value("${GCS.bucket-name}") String bucketName, CityRepository cityRepository) {

        this.placeService = placeService;
        this.gcsService = gcsService;
        this.bucketName = bucketName;
        this.cityRepository = cityRepository;
    }

    @GetMapping("/places")
    public PlacesResponseBody getPlaces(@RequestParam(value="city") String city, @RequestParam(value="start_date") String startDate, @RequestParam(value="end_date") String endDate) {
        Pair<String, List<Place>> result = placeService.placeSearch(city, startDate, endDate);
        if (result.getFirst().isEmpty()) throw new IllegalArgumentException("Failed to get places");
        return new PlacesResponseBody(result.getFirst(), result.getSecond());
    }

}
