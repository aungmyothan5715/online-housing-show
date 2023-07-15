package com.example.backendapi.domain.repo;

import com.example.backendapi.domain.model.Housing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HousingRepo extends JpaRepository<Housing, Integer> {
    Page<Housing> findAll(Pageable pageable);
    Page<Housing> findByHousingNameIgnoreCaseContaining(String housingName, Pageable pageable);
}
