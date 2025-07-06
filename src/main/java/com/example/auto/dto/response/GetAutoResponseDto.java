package com.example.auto.dto.response;

import com.example.auto.model.ServiceCompany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public class GetAutoResponseDto {

    private String brandName;
    private String modelName;
    private List<ServiceCompany> serviceCompanies;

    public GetAutoResponseDto(String brandName, String modelName, List<ServiceCompany> serviceCompanies) {
        this.brandName = brandName;
        this.modelName = modelName;
        this.serviceCompanies = serviceCompanies;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public List<ServiceCompany> getServiceCompanies() {
        return serviceCompanies;
    }

    public void setServiceCompanies(List<ServiceCompany> serviceCompanies) {
        this.serviceCompanies = serviceCompanies;
    }
}
