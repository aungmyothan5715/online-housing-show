package com.example.backendapi.api;

import com.example.backendapi.domain.dto.HousingRequest;
import com.example.backendapi.domain.model.Housing;
import com.example.backendapi.domain.model.Owner;
import com.example.backendapi.domain.page.PaginationDto;
import com.example.backendapi.domain.repo.OwnerRepo;
import com.example.backendapi.domain.service.HousingService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/private")
public class PrivateApiController {
    @Autowired
    private HousingService housingService;
    @Autowired
    private OwnerRepo ownerRepo;
    @Value("${my.app.sKey}")
    private String sKey;

    @PostMapping("/create")
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
        return ResponseEntity.ok("Housing Post is created by ");
    }

    @PostMapping("/housing/update")
    //@PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> update(@Validated @RequestBody HousingRequest request, Authentication authentication, BindingResult bindingResult) {
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



}
