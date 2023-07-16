package com.example.backendapi.domain.service;

import com.example.backendapi.domain.dto.HousingRequest;
import com.example.backendapi.domain.model.Housing;
import com.example.backendapi.domain.model.Owner;
import com.example.backendapi.domain.page.PaginationDto;
import com.example.backendapi.domain.repo.HousingRepo;
import com.example.backendapi.domain.repo.OwnerRepo;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HousingService {
    @Autowired
    private HousingRepo housingRepo;
    @Autowired
    private OwnerRepo ownerRepo;

    @Transactional
    public void saveHousing(Housing request, HttpHeaders headers, String sKey) {
        //        Getting username from token
        String token = headers.get("Authorization").get(0);
        String jwt = token.replace("Bearer","");
        //Exteact jwt token
        String email = Jwts.parser().setSigningKey(sKey).parseClaimsJws(jwt).getBody().getSubject();
        // Find owner email from database
        Owner owner = ownerRepo.findOwnerByOwnerEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username is not found!"));

        Housing housing = new Housing();
        housing.setHousingName(request.getHousingName());
        housing.setAddress(request.getAddress());
        housing.setNumberOfFloor(request.getNumberOfFloor());
        housing.setNumberOfMasterRoom(request.getNumberOfMasterRoom());
        housing.setNumberOfSingleRoom(request.getNumberOfSingleRoom());
        housing.setAmount(request.getAmount());

        housing.setCreatedDate(LocalDateTime.now());
        housing.setUpdatedDate(LocalDateTime.now());
        housing.setOwner(owner);
        housingRepo.save(housing);
    }
    @Transactional
    public void updateHousing(int id, HousingRequest request, HttpHeaders headers, String sKey) {
        //        Getting username from token
        String token = headers.get("Authorization").get(0);
        String jwt = token.replace("Bearer","");
        //Exteact jwt token
        String email = Jwts.parser().setSigningKey(sKey).parseClaimsJws(jwt).getBody().getSubject();
        // Find owner email from database
        Owner owner = ownerRepo.findOwnerByOwnerEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username is not found!"));
        // Find housing by id from database
        Housing housing = housingRepo.findById(id).orElseThrow();

        housing.setHousingName(request.getHousingName());
        housing.setAddress(request.getAddress());
        housing.setNumberOfFloor(request.getNumberOfFloor());
        housing.setNumberOfMasterRoom(request.getNumberOfMasterRoom());
        housing.setNumberOfSingleRoom(request.getNumberOfSingleRoom());
        housing.setAmount(request.getAmount());
        housing.setOwner(owner);
        housing.setUpdatedDate(LocalDateTime.now());

        // Update Housing post
        housingRepo.save(housing);
    }

// Public API
    public PaginationDto<Housing> getHousing(
            Optional<String> housingName,
            Optional<String> address,
            Optional<Integer> numFloor,
            Optional<Integer> numMasterRoom,
            Optional<Integer> numSingleRoom,
            Optional<Integer> amount,
            Optional<LocalDateTime> createdDate,
            Optional<LocalDateTime> updatedDate,
            int pageNum, int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Housing> housingPage = housingRepo.searchHousings(housingName, address, numFloor,numMasterRoom, numSingleRoom, amount,createdDate, updatedDate, pageable);
        PaginationDto<Housing> paginationDto = new PaginationDto<>();
        paginationDto.setData(housingPage.getContent());
        paginationDto.setTotalPages(housingPage.getTotalPages());
        paginationDto.setTotalElements(housingPage.getTotalElements());
        return paginationDto;
    }

    // for private
    public PaginationDto<Housing> findAllHousingForOwner(
            HttpHeaders headers, String sKey,
            Optional<String> housingName,
            Optional<String> address,
            Optional<Integer> numFloor,
            Optional<Integer> numMasterRoom,
            Optional<Integer> numSingleRoom,
            Optional<Integer> amount,
            Optional<LocalDateTime> createdDate,
            Optional<LocalDateTime> updatedDate,
            int pageNum, int pageSize) {
        String token = headers.get("Authorization").get(0);
        String jwt = token.replace("Bearer","");
        String ownerUserName = Jwts.parser().setSigningKey(sKey).parseClaimsJws(jwt).getBody().getSubject();

        Owner owner = ownerRepo.findOwnerByOwnerEmail(ownerUserName).orElseThrow();
        System.out.println(ownerUserName);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Housing> housingPage = housingRepo.searchHousingForOwner(housingName, address, numFloor, numMasterRoom, numSingleRoom, amount,owner, createdDate, updatedDate, pageable);

        System.out.println(housingPage);

        PaginationDto<Housing> paginationDto = new PaginationDto<>();
        paginationDto.setData(housingPage.getContent());
        paginationDto.setTotalPages(housingPage.getTotalPages());
        paginationDto.setTotalElements(housingPage.getTotalElements());
        return paginationDto;
    }

}
