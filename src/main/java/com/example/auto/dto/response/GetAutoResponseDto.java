package com.example.auto.dto.response;

import com.example.auto.dto.redis.ServiceCompanyCacheDto;
import com.example.auto.model.ServiceCompany;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


public class GetAutoResponseDto implements Serializable {

    private String brandName;
    private String modelName;
    private List<ServiceCompanyCacheDto> serviceCompanies;

    public GetAutoResponseDto() {
    }

    public GetAutoResponseDto(String brandName, String modelName, List<ServiceCompanyCacheDto> serviceCompanies) {
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

    public List<ServiceCompanyCacheDto> getServiceCompanies() {
        return serviceCompanies;
    }

    public void setServiceCompanies(List<ServiceCompanyCacheDto> serviceCompanies) {
        this.serviceCompanies = serviceCompanies;
    }
}
