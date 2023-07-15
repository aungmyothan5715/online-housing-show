package com.example.backendapi.domain.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Setter
@Getter
@Table(name = "housing")
public class Housing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = true, name = "housing_name")
    private String housingName;
    // @Column(nullable = false)
    private String address;
    @Column(nullable = false, name = "number_of_floor")
    private int numberOfFloor;
    @Column(nullable = false, name = "number_of_master_room")
    private int numberOfMasterRoom;
    @Column(nullable = false, name = "number_of_single_room")
    private int numberOfSingleRoom;
    @Column(nullable = false)
    private int amount;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;
    public Housing() {}

}
