package com.example.auto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nameModel;

    private String vinCode;

    private String brand;

    @ManyToMany(mappedBy = "autos")
    private List<ServiceCompany> serviceCompanies = new ArrayList<>();
}
