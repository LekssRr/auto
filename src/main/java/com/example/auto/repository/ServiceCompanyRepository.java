package com.example.auto.repository;

import com.example.auto.model.ServiceCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCompanyRepository extends JpaRepository<ServiceCompany, Integer> {
}
