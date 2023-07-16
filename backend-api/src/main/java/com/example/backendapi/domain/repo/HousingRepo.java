package com.example.backendapi.domain.repo;

import com.example.backendapi.domain.model.Housing;
import com.example.backendapi.domain.model.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HousingRepo extends JpaRepository<Housing, Integer> {
    Page<Housing> findAll(Pageable pageable);

    @Query("SELECT p FROM Housing p WHERE " +
            "(:housingName IS NULL OR LOWER(p.housingName) LIKE %:housingName%) AND " +
            "(:address IS NULL OR LOWER(p.address) LIKE %:address%) AND " +
            "(:numFloor IS NULL OR p.numberOfFloor = :numFloor) AND " +
            "(:numMasterRoom IS NULL OR p.address = :numMasterRoom) AND " +
            "(:numSingleRoom IS NULL OR p.numberOfSingleRoom = :numSingleRoom) AND " +
            "(:amount IS NULL OR p.amount = :amount) AND " +
            "(:createdDate IS NULL OR p.createdDate = :createdDate) AND " +
            "(:updatedDate IS NULL OR p.updatedDate = :updatedDate)"
    )
    Page<Housing> searchHousings(
            @Param("housingName") Optional<String> housingName,
            @Param("address") Optional<String> address,
            @Param("numFloor") Optional<Integer> numFloor,
            @Param("numMasterRoom") Optional<Integer> numMasterRoom,
            @Param("numSingleRoom") Optional<Integer> numSingleRoom,
            @Param("amount") Optional<Integer> amount,
            @Param("createdDate") Optional<LocalDateTime> createdDate,
            @Param("updatedDate") Optional<LocalDateTime> updatedDate,
            Pageable pageable);



    @Query("SELECT p FROM Housing p WHERE" +
            "(:housingName IS NULL OR LOWER(p.housingName) LIKE %:housingName%) AND " +
            "(:address IS NULL OR LOWER(p.address) LIKE %:address%) AND " +
            "(:numFloor IS NULL OR p.numberOfFloor = :numFloor) AND " +
            "(:numMasterRoom IS NULL OR p.address = :numMasterRoom) AND " +
            "(:numSingleRoom IS NULL OR p.numberOfSingleRoom = :numSingleRoom) AND " +
            "(:amount IS NULL OR p.amount = :amount) AND " +
            "(:owner IS NULL OR  p.owner = :owner) AND " +
            "(:createdDate IS NULL OR p.createdDate = :createdDate) AND " +
            "(:updatedDate IS NULL OR p.updatedDate = :updatedDate)"
    )
    Page<Housing> searchHousingForOwner(
            @Param("housingName") Optional<String>housingName,
            @Param("address") Optional<String> address,
            @Param("numFloor") Optional<Integer> numFloor,
            @Param("numMasterRoom") Optional<Integer> numMasterRoom,
            @Param("numSingleRoom") Optional<Integer> numSingleRoom,
            @Param("amount") Optional<Integer> amount,
            @Param("owner") Owner owner,
            @Param("createdDate") Optional<LocalDateTime> createdDate,
            @Param("updatedDate") Optional<LocalDateTime> updatedDate,
            Pageable pageable
    );


}


