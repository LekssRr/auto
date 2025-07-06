package com.example.auto.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nameModel;

    private String vinCode;

    private String brand;

    @ManyToMany(mappedBy = "autos")
    private List<ServiceCompany> serviceCompanies = new ArrayList<>();

    public Auto(String nameModel, String vinCode, String brand, List<ServiceCompany> serviceCompanies) {
        this.nameModel = nameModel;
        this.vinCode = vinCode;
        this.brand = brand;
        this.serviceCompanies = serviceCompanies;
    }

    public Auto() {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameModel() {
        return nameModel;
    }

    public void setNameModel(String nameModel) {
        this.nameModel = nameModel;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<ServiceCompany> getServiceCompanies() {
        return serviceCompanies;
    }

    public void setServiceCompanies(List<ServiceCompany> serviceCompanies) {
        this.serviceCompanies = serviceCompanies;
    }
}
