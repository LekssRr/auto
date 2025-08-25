package com.example.auto.repository;

import com.example.auto.model.Auto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutoRepository extends JpaRepository<Auto, Integer> {

    @EntityGraph(attributePaths = {"serviceCompanies"})
    Optional<Auto> findAutoByVinCode(String vinCode);


}
