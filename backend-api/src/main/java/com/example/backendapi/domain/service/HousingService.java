package com.example.backendapi.domain.service;

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
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HousingService {
    @Autowired
    private HousingRepo housingRepo;
    @Autowired
    private OwnerRepo ownerRepo;

    public void saveHousing(Housing request, HttpHeaders headers, String sKey) {
        //        Getting username from token
        String token = headers.get("Authorization").get(0);
        String jwt = token.replace("Bearer","");
        String email = Jwts.parser().setSigningKey(sKey).parseClaimsJws(jwt).getBody().getSubject();

        Housing housing = new Housing();
        housing.setHousingName(request.getHousingName());
        housing.setAddress(request.getAddress());
        housing.setNumberOfFloor(request.getNumberOfFloor());
        housing.setNumberOfMasterRoom(request.getNumberOfMasterRoom());
        housing.setNumberOfSingleRoom(request.getNumberOfSingleRoom());
        housing.setAmount(request.getAmount());
        Owner owner = ownerRepo.findOwnerByOwnerEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username is not found!"));
        housing.setOwner(owner);
        housing.setCreatedDate(LocalDateTime.now());
        housing.setUpdatedDate(LocalDateTime.now());

        housingRepo.save(request);
    }

    public PaginationDto<Housing> getAllHousing(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Housing> housingPage = housingRepo.findAll(pageable);

        PaginationDto<Housing> paginationDto = new PaginationDto<>();
        paginationDto.setData(housingPage.getContent());
        paginationDto.setTotalPages(housingPage.getTotalPages());
        paginationDto.setTotalElements(housingPage.getTotalElements());
        return paginationDto;
    }

    public PaginationDto<Housing> searchByHousingName(String housingName, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Housing> housingPage = housingRepo.findByHousingNameIgnoreCaseContaining(housingName, pageable);

        PaginationDto<Housing> paginationDto = new PaginationDto<>();
        paginationDto.setData(housingPage.getContent());
        paginationDto.setTotalPages(housingPage.getTotalPages());
        paginationDto.setTotalElements(housingPage.getTotalElements());
        return paginationDto;
    }
    public PaginationDto<Housing> searchByAmount(String amount, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Housing> housingPage = housingRepo.findByHousingNameIgnoreCaseContaining(amount, pageable);

        PaginationDto<Housing> paginationDto = new PaginationDto<>();
        paginationDto.setData(housingPage.getContent());
        paginationDto.setTotalPages(housingPage.getTotalPages());
        paginationDto.setTotalElements(housingPage.getTotalElements());
        return paginationDto;
    }


}
