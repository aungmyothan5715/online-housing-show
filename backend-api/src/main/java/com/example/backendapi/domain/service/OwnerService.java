package com.example.backendapi.domain.service;

import com.example.backendapi.domain.dto.LoginRequest;
import com.example.backendapi.domain.dto.LoginResponse;
import com.example.backendapi.domain.dto.RegisterRequest;
import com.example.backendapi.domain.model.Owner;
import com.example.backendapi.domain.repo.OwnerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OwnerService {
    @Autowired
    private OwnerRepo ownerRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // for register
    public void saveOwner(String ownerUserName, String ownerName, String ownerEmail, String pass) {

            Owner owner = new Owner();
            owner.setOwnerUserName(ownerUserName);
            owner.setOwnerName(ownerName);
            owner.setOwnerEmail(ownerEmail);
            owner.setPassword(passwordEncoder.encode(pass));
            owner.setCreatedDate(LocalDateTime.now());
            owner.setUpdatedDate(LocalDateTime.now());
            ownerRepo.save(owner);
            var jwtToken = jwtService.generateToken(owner);

    }

    // for login
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getOwnerEmail(),request.getPassword())
        );
        var user = ownerRepo.findOwnerByOwnerEmail(request.getOwnerEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + jwtToken);
        return LoginResponse.builder()
                .ownerEmail(request.getOwnerEmail())
                .password(request.getPassword())
                .token(jwtToken).build();
    }

}
