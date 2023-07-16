package com.example.backendapi.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping
    public ResponseEntity<?> link(){

        return ResponseEntity.ok("OK");

        //localhost:8080/api/public/register
        //localhost:8080/api/public/login
        //localhost:8080/api/public/housing/search
        //localhost:8080/api/public/housing/search?address=YGN
        //localhost:8080/api/public/housing/search?housingName=Grand Hanter   <-- if you want to search --> ..../search?address=ygn
        //localhost:8080/api/private/auth/post/create
        //localhost:8080/api/private/auth/post/update/26
        //localhost:8080/api/private/auth/all/search
        //localhost:8080/api/private/auth/all/search?address=Pyay
        //localhost:8080/api/private/auth/all/search?amount=50000
        //localhost:8080/api/private/auth/all/search?numFloor=3
    }

}
