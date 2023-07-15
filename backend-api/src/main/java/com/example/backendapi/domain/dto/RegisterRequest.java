package com.example.backendapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private int id;
    @NotBlank(message = "User name is required!")
    private String ownerUserName;
    @NotBlank(message = "Name is required!")
    private String ownerName;
    @NotBlank(message = "Email is required!")
    private String ownerEmail;
    @NotBlank(message = "Password is required!")
    private String password;
}
