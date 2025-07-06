package com.example.auto.controller;

import com.example.auto.service.ServiceCompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/servicecompany")
public class ServiceCompanyController {

    private final ServiceCompanyService serviceCompanyService;

    public ServiceCompanyController(ServiceCompanyService serviceCompanyService) {
        this.serviceCompanyService = serviceCompanyService;
    }

    @GetMapping("/")
    public ResponseEntity<String> getServiceCompany() {
        return ResponseEntity.ok("3");
    }

    @PostMapping("/")
    public ResponseEntity<String> postServiceCompany() {
        return ResponseEntity.ok("1");
    }

}
