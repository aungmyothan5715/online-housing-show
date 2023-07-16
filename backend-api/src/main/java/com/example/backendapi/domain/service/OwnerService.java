package com.example.backendapi.domain.service;

import com.example.backendapi.domain.dto.RegisterRequest;
import com.example.backendapi.domain.model.Owner;
import com.example.backendapi.domain.repo.OwnerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OwnerService {
    @Autowired
    private OwnerRepo ownerRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveOwner(String ownerUserName, String ownerName, String ownerEmail, String pass) {

            Owner owner = new Owner();
            owner.setOwnerUserName(ownerUserName);
            owner.setOwnerName(ownerName);
            owner.setOwnerEmail(ownerEmail);
            owner.setPassword(passwordEncoder.encode(pass));
            owner.setCreatedDate(LocalDateTime.now());
            owner.setUpdatedDate(LocalDateTime.now());

            ownerRepo.save(owner);

    }

}
