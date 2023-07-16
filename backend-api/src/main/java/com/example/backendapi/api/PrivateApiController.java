package com.example.backendapi.api;

import com.example.backendapi.domain.dto.HousingRequest;
import com.example.backendapi.domain.model.Housing;
import com.example.backendapi.domain.model.Owner;
import com.example.backendapi.domain.page.PaginationDto;
import com.example.backendapi.domain.repo.HousingRepo;
import com.example.backendapi.domain.repo.OwnerRepo;
import com.example.backendapi.domain.service.HousingService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/private/auth")
public class PrivateApiController {
    @Autowired
    private HousingService housingService;
    @Value("${my.app.sKey}")
    private String sKey;

    @PostMapping("/post/create")
    // @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> create(@Validated @RequestBody Housing request,@RequestHeader HttpHeaders headers, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        housingService.saveHousing(request, headers, sKey);
        return ResponseEntity.ok().body("Housing post created successfully.");
    }

    @PostMapping("/post/update/{id}")
    //@PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody HousingRequest request, @RequestHeader HttpHeaders headers, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        housingService.updateHousing(id, request, headers, sKey);
        return ResponseEntity.ok(request);
    }
    @GetMapping("/test")
    public void test(@RequestHeader HttpHeaders headers, String sKey){
        System.out.println(sKey);
    }
    @GetMapping("/search")
    public PaginationDto<Housing> findAllHousingForOwner(@RequestHeader HttpHeaders headers,
                                                         @RequestParam Optional<String> housingName,
                                                         @RequestParam Optional<String> address,
                                                         @RequestParam Optional<Integer> numFloor,
                                                         @RequestParam Optional<Integer> numMasterRoom,
                                                         @RequestParam Optional<Integer> numSingleRoom,
                                                         @RequestParam Optional<Integer> amount,
                                                         @RequestParam Optional<LocalDateTime> createdDate,
                                                         @RequestParam Optional<LocalDateTime> updatedDate,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size
                                                         ){
        return housingService.findAllHousingForOwner(headers, sKey, housingName, address, numFloor, numMasterRoom, numSingleRoom, amount, createdDate, updatedDate, page, size);
    }

// List all for Owner

    //localhost:8080/api/public/housing/search?createdDate=2023-07-16 18:23:21.818306  for LocalDateTime
    @GetMapping("/all/search")
    public PaginationDto<Housing> searchHousings(
            @RequestHeader HttpHeaders headers,
            @RequestParam Optional<String> housingName,
            @RequestParam Optional<String> address,
            @RequestParam Optional<Integer> numFloor,
            @RequestParam Optional<Integer> numMasterRoom,
            @RequestParam Optional<Integer> numSingleRoom,
            @RequestParam Optional<Integer> amount,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") Optional<LocalDateTime> createdDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") Optional<LocalDateTime> updatedDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return housingService.findAllHousingForOwner(headers, sKey, housingName, address, numFloor, numMasterRoom, numSingleRoom, amount, createdDate, updatedDate, page, size);
    }






}
