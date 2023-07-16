package com.example.backendapi.domain.service;

import com.example.backendapi.domain.dto.LoginRequest;
import com.example.backendapi.domain.dto.LoginResponse;
import com.example.backendapi.domain.repo.OwnerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final OwnerRepo ownerRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getOwnerEmail(),request.getPassword())
        );
        var user = ownerRepo.findOwnerByOwnerEmail(request.getOwnerEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return LoginResponse.builder()
                .ownerEmail(request.getOwnerEmail())
                .password(request.getPassword())
                .token(jwtToken).build();
    }
}
