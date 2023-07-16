package com.example.backendapi.api;

import com.example.backendapi.domain.dto.LoginRequest;
import com.example.backendapi.domain.model.Housing;
import com.example.backendapi.domain.page.PaginationDto;
import com.example.backendapi.domain.service.HousingService;
import com.example.backendapi.domain.service.JwtService;
import com.example.backendapi.domain.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    //search by address , name , floor , createdDate

    //http://localhost:8080/api/public/housing/search?name=one  <- one is keyword
    //localhost:8080/api/public/housing/search?name=dream&page=1  <- search with pagination
    @GetMapping("/housing/search")
    public PaginationDto<Housing> searchByHousingName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return housingService.searchByHousingName(name, page, size);
    }

//    @GetMapping("/housing/search")
//    public PaginationDto<Housing> searchByAmount(
//            @RequestParam String amount,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size) {
//        return housingService.searchByAmount(amount, page, size);
//    }




}
