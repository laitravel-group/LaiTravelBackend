package com.laitravel.laitravelbe;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

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

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers(HttpMethod.GET, "/places","/trip-plans", "trip-plan-details").permitAll()
                                .requestMatchers(HttpMethod.POST, "/trip-plan-build", "/trip-plan-build-update", "/trip-plan-save").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/trip-plan-delete").permitAll()
                                .requestMatchers("/hello/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/login", "/register", "/logout","/trip-plan-build").permitAll()
                                .anyRequest().authenticated()
                );
//                .exceptionHandling()
//                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                .and()
//                .formLogin()
//                .successHandler((req, res, auth) -> res.setStatus(HttpStatus.NO_CONTENT.value()))
//                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
//                .and()
//                .logout()
//                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT));
        return http.build();
    }



}

