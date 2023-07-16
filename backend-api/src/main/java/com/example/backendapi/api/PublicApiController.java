package com.example.backendapi.api;

import com.example.backendapi.domain.dto.LoginRequest;
import com.example.backendapi.domain.dto.LoginResponse;
import com.example.backendapi.domain.model.Housing;
import com.example.backendapi.domain.page.PaginationDto;
import com.example.backendapi.domain.service.HousingService;
import com.example.backendapi.domain.service.JwtService;
import com.example.backendapi.domain.service.OwnerService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@Validated
public class PublicApiController {
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private HousingService housingService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> register(@RequestParam("ownerUserName") String ownerUserName,
                                      @RequestParam("ownerName") String ownerName,
                                      @RequestParam("ownerEmail")String ownerEmail,
                                      @RequestParam("password")String password
                                      ) {
        ownerService.saveOwner(ownerUserName, ownerName, ownerEmail, password);
        return ResponseEntity.ok().body("Register Successful.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        return ResponseEntity.ok(ownerService.login(request));
    }

    //http://localhost:8080/api/public/housing/search/address?address=one  <- one is keyword
    //localhost:8080/api/public/housing/search/address?address=dream&page=1  <- search with pagination

    //localhost:8080/api/public/housing/search?createdDate=2023-07-16 18:23:21.818306  for LocalDateTime
    @GetMapping("/housing/search")
    public PaginationDto<Housing> searchHousings(
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
        return housingService.getHousing(housingName, address, numFloor, numMasterRoom, numSingleRoom, amount, createdDate, updatedDate, page, size);
    }

}
