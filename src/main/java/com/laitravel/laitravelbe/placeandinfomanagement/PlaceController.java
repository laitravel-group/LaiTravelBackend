package com.laitravel.laitravelbe.placeandinfomanagement;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceController {
    final PlaceService placeService;
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    





}
