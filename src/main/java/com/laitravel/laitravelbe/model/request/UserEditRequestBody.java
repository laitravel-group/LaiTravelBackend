package com.laitravel.laitravelbe.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

public record UserEditRequestBody(
        String username,
        @JsonProperty("display_name") String displayName,
        String password,
        MultipartFile avatar
) {
}
