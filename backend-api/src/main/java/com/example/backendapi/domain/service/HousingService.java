package com.example.backendapi.domain.service;

import com.example.backendapi.domain.model.Housing;
import com.example.backendapi.domain.model.Owner;
import com.example.backendapi.domain.page.PaginationDto;
import com.example.backendapi.domain.repo.HousingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HousingService {
    @Autowired
    private HousingRepo housingRepo;

    public void saveHousing(Housing request) {
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


}
