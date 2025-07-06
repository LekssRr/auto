package com.example.auto.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
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

    public ServiceCompany(Integer id, String nameServiceCompany, String description, List<Auto> autos) {
        this.id = id;
        this.nameServiceCompany = nameServiceCompany;
        this.description = description;
        this.autos = autos;
    }

    public ServiceCompany() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameServiceCompany() {
        return nameServiceCompany;
    }

    public void setNameServiceCompany(String nameServiceCompany) {
        this.nameServiceCompany = nameServiceCompany;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Auto> getAutos() {
        return autos;
    }

    public void setAutos(List<Auto> autos) {
        this.autos = autos;
    }
}
