package com.laitravel.laitravelbe;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AppConfig {

    @Bean
    public GeoApiContext GeoApiContext(@Value("${google.map-key}") String mapKey){
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(mapKey)
                .build();
        return context;
    }
    @Bean
    public Storage storage(@Value("${GCS.project-id}") String projectID ) throws IOException {

        return StorageOptions.newBuilder()
                .setProjectId(projectID)
                .build()
                .getService();
    }




}
