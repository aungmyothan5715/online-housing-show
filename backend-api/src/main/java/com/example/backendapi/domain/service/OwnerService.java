package com.example.backendapi.domain.service;

import com.example.backendapi.domain.dto.RegisterRequest;
import com.example.backendapi.domain.model.Owner;
import com.example.backendapi.domain.repo.OwnerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OwnerService {
    @Autowired
    private OwnerRepo ownerRepo;


    public void saveOwner(Owner user) {

        ownerRepo.save(user);
    }
    public boolean isEmailValid(String email) {
        Owner user = ownerRepo.findOwnerByOwnerEmail(email);
        if (user == null) {
            return false;
        }

        return true;
    }
}
