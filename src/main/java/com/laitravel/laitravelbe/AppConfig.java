package com.laitravel.laitravelbe;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public GeoApiContext GeoApiContext (@Value("${google.map-key}") String mapKey){
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(mapKey)
                .build();
        return context;
    }

}