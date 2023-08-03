package com.laitravel.laitravelbe.user;

import com.laitravel.laitravelbe.auth.AuthenticationRequest;
import com.laitravel.laitravelbe.auth.AuthenticationResponse;
import com.laitravel.laitravelbe.auth.AuthenticationService;
import com.laitravel.laitravelbe.model.request.UserEditRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    private final AuthenticationService authenticationService;

    public RegisterController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public AuthenticationResponse registerUser(@RequestBody UserEditRequestBody body) {
        return authenticationService.register(body);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


}

