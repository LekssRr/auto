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
public class ServiceCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nameServiceCompany;

    private String description;
    @ManyToMany
    @JoinTable(
            name = "service_company_auto",
            joinColumns = @JoinColumn(name = "service_company_id"),
            inverseJoinColumns = @JoinColumn(name = "auto_id")
    )
    private List<Auto> autos = new ArrayList<>();

}
