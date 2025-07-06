package com.example.auto.dto.request;

public class ServiceCompanyRequestDto {

    private String nameServiceCompany;

    private String description;

    public ServiceCompanyRequestDto(String nameServiceCompany, String description) {
        this.nameServiceCompany = nameServiceCompany;
        this.description = description;
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
}
