package com.laitravel.laitravelbe.place;

import com.laitravel.laitravelbe.gcs.GCSService;
import com.laitravel.laitravelbe.model.Place;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlaceController {
    final PlaceService placeService;
    final GCSService gcsService;
    final String bucketName;
    public PlaceController(PlaceService placeService, GCSService gcsService,@Value("${GCS.bucket-name}") String bucketName) {

        this.placeService = placeService;
        this.gcsService = gcsService;
        this.bucketName = bucketName;
    }

    @GetMapping("/places")
    public List<Place> getPlaces(@RequestParam(value="city") String city, @RequestParam(value="startDate") String startDate, @RequestParam(value="endDate") String endDate) {
        return placeService.placeSearch(city, startDate, endDate);
    }




}
