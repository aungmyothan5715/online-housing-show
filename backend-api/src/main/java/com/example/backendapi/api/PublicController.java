package com.example.backendapi.api;

import com.example.backendapi.domain.dto.HousingRequest;
import com.example.backendapi.domain.dto.LoginRequest;
import com.example.backendapi.domain.model.Housing;
import com.example.backendapi.domain.model.Owner;
import com.example.backendapi.domain.page.PaginationDto;
import com.example.backendapi.domain.repo.OwnerRepo;
import com.example.backendapi.domain.service.HousingService;
import com.example.backendapi.domain.service.OwnerService;
import com.example.backendapi.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@Validated
public class PublicController {

    @Autowired
    private OwnerService ownerService;
    @Autowired
    private OwnerRepo ownerRepo;
    @Autowired
    private HousingService housingService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> register(@RequestParam("ownerUserName") String ownerUserName,
                                      @RequestParam("ownerName") String ownerName,
                                      @RequestParam("ownerEmail")String ownerEmail,
                                      @RequestParam("password")String password
                                      ) {

        boolean isValid = ownerService.isEmailValid(ownerEmail);

        if (isValid) {
            throw new RuntimeException("Email is invalid!");
            //return ResponseEntity.ok("Email is invalid!");
        }


        Owner owner = new Owner();
        owner.setOwnerUserName(ownerUserName);
        owner.setOwnerName(ownerName);
        owner.setOwnerEmail(ownerEmail);
        owner.setPassword(passwordEncoder.encode(password));
        owner.setCreatedDate(LocalDateTime.now());
        owner.setUpdatedDate(LocalDateTime.now());

        ownerService.saveOwner(owner);

        return ResponseEntity.ok().body(owner);

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

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getOwnerEmail(), request.getPassword())
            );

            //get Username
            String username = authentication.getName();

            //Generate token from tokenProvider
            String token = jwtTokenProvider.generateToken(username);

            //Set the token response on header
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);

            //Authentication successful.


            return ResponseEntity.ok().headers(headers).body(username + " is Login successful.");
        }catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }

    }

    @PostMapping("/create")
   // @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> create(@Validated @RequestBody Housing request,Authentication authentication, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }

        //check authentication
        if (authentication != null && authentication.isAuthenticated()) {
            //get username or email
            String email = authentication.getName();

            //retrieve the user from database
            Owner owner = ownerRepo.findOwnerByOwnerEmail(email);
            //create post
            Housing housingPost = new Housing();
            housingPost.setHousingName(request.getHousingName());
            housingPost.setAddress(request.getAddress());
            housingPost.setNumberOfFloor(request.getNumberOfFloor());
            housingPost.setNumberOfMasterRoom(request.getNumberOfMasterRoom());
            housingPost.setNumberOfSingleRoom(request.getNumberOfSingleRoom());
            housingPost.setAmount(request.getAmount());
            housingPost.setCreatedDate(LocalDateTime.now());
            housingPost.setUpdatedDate(LocalDateTime.now());
            housingPost.setOwner(owner);

            //save housinig
            housingService.saveHousing(housingPost);
            return ResponseEntity.ok().body("Post created by " + owner);

        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated!");
        }

    }

    @PostMapping("/housing/update")
    //@PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> update(@Validated @RequestBody HousingRequest request,Authentication authentication, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }




        return ResponseEntity.ok(request);
    }

    //http://localhost:8080/api/public/housing/all-list?page=0&size=5
    @GetMapping("/housing/all-list")
    public PaginationDto<Housing> getAllHousing(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return housingService.getAllHousing(page, size);
    }


    //search by address , name , floor , createdDate

    //http://localhost:8080/api/public/housing/search?name=one  <- one is keyword
    @GetMapping("/housing/search")
    public PaginationDto<Housing> searchByHousingName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return housingService.searchByHousingName(name, page, size);
    }




}
