package com.example.backendapi.domain.repo;

import com.example.backendapi.domain.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepo extends JpaRepository<Owner, Integer> {
    Owner findOwnerByOwnerEmail(String email);
}
