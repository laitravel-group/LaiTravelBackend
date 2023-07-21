package com.laitravel.laitravelbe.auth;

import com.laitravel.laitravelbe.auth.AuthenticationRequest;
import com.laitravel.laitravelbe.auth.AuthenticationResponse;
import com.laitravel.laitravelbe.auth.jwt.JwtService;
import com.laitravel.laitravelbe.db.UserRepository;
import com.laitravel.laitravelbe.db.entity.UserEntity;
import com.laitravel.laitravelbe.model.request.UserEditRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsManager userDetailsManager;

    public AuthenticationResponse register(UserEditRequestBody request) {
        UserDetails user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .roles("USER")
                .build();
        userDetailsManager.createUser(user);
        userRepository.updateNameByUserId(request.username(),request.displayName(), request.avatar());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userDetailsManager.loadUserByUsername(request.getUsername());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}