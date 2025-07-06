package com.example.auto.dto.request;


public class AutoRequestDto {

    private String modelName;
    private String vinCode;
    private String brandName;

    public AutoRequestDto(String modelName, String vinCode, String brandName) {
        this.modelName = modelName;
        this.vinCode = vinCode;
        this.brandName = brandName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
