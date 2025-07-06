package com.example.auto.dto.redis;

import java.io.Serializable;

public class ServiceCompanyCacheDto implements Serializable {
    private Integer id;
    private String name;
    private String description;
    public ServiceCompanyCacheDto() {
    }
    public ServiceCompanyCacheDto(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
