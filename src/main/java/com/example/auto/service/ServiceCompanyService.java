package com.example.auto.service;

import com.example.auto.dto.request.ServiceCompanyRequestDto;
import com.example.auto.dto.response.GetServiceCompanyResponseDto;
import com.example.auto.dto.response.PostServiceCompanyResponseDto;

public interface ServiceCompanyService {

    GetServiceCompanyResponseDto getServiceCompany(ServiceCompanyRequestDto serviceCompanyRequestDto);

    PostServiceCompanyResponseDto postServiceCompany(ServiceCompanyRequestDto serviceCompanyRequestDto);

}
