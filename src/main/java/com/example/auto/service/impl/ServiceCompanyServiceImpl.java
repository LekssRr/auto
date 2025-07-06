package com.example.auto.service.impl;

import com.example.auto.dto.request.ServiceCompanyRequestDto;
import com.example.auto.dto.response.GetServiceCompanyResponseDto;
import com.example.auto.dto.response.PostServiceCompanyResponseDto;
import com.example.auto.repository.ServiceCompanyRepository;
import com.example.auto.service.ServiceCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceCompanyServiceImpl implements ServiceCompanyService {

    private final ServiceCompanyRepository serviceCompanyRepository;

    @Autowired
    public ServiceCompanyServiceImpl(ServiceCompanyRepository serviceCompanyRepository) {
        this.serviceCompanyRepository = serviceCompanyRepository;
    }

    @Override
    public GetServiceCompanyResponseDto getServiceCompany(ServiceCompanyRequestDto serviceCompanyRequestDto) {
        return null;
    }

    @Override
    public PostServiceCompanyResponseDto postServiceCompany(ServiceCompanyRequestDto serviceCompanyRequestDto) {
        return null;
    }
}
