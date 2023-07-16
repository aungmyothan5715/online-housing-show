package com.example.backendapi.domain.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HousingRequest {
    @NotBlank(message = "Housing name is required!")
    private String housingName;
    @NotBlank(message = "Address is required!")
    private String address;
    @NotBlank(message = "Number of Floor is required!")
    private int numberOfFloor;
    @NotBlank(message = "Number of Master room is required!")
    private int numberOfMasterRoom;
    @NotBlank(message = "Number of Single room is required!")
    private int numberOfSingleRoom;
    @NotBlank(message = "Amount is required!")
    private int amount;

}
