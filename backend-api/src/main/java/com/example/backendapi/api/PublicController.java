package com.example.backendapi.api;

import com.example.backendapi.domain.dto.HousingRequest;
import com.example.backendapi.domain.dto.LoginRequest;
import com.example.backendapi.domain.model.Housing;
import com.example.backendapi.domain.model.Owner;
import com.example.backendapi.domain.page.PaginationDto;
import com.example.backendapi.domain.repo.OwnerRepo;
import com.example.backendapi.domain.service.AuthenticationService;
import com.example.backendapi.domain.service.HousingService;
import com.example.backendapi.domain.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@Validated
public class PublicController {

    @Autowired
    private AuthenticationService authService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private OwnerRepo ownerRepo;
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

        return ResponseEntity.ok().body("Register Successfull.");


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
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/create")
   // @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> create(@Validated @RequestBody Housing request, @CurrentSecurityContext UserPrincipal userPrincipal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }

        //check authentication
        //retrieve the user from database
        Owner owner = ownerRepo.findOwnerByOwnerEmail(userPrincipal.getName()).orElseThrow(() ->new UsernameNotFoundException("Username "+userPrincipal.getName()+" not found!"));

        if (userPrincipal.getName() != null && owner.getOwnerEmail() != null) {
            //get username or email
            String email = userPrincipal.getName();


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
    //localhost:8080/api/public/housing/search?name=dream&page=1  <- search with pagination
    @GetMapping("/housing/search")
    public PaginationDto<Housing> searchByHousingName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return housingService.searchByHousingName(name, page, size);
    }

    @GetMapping("/housing/search")
    public PaginationDto<Housing> searchByAmount(
            @RequestParam String amount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return housingService.searchByAmount(amount, page, size);
    }




}
