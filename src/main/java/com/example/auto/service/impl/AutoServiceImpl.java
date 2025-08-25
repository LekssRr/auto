package com.example.auto.service.impl;

import com.example.auto.dto.redis.ServiceCompanyCacheDto;
import com.example.auto.dto.request.AutoRequestDto;
import com.example.auto.dto.response.GetAutoResponseDto;
import com.example.auto.dto.response.PostAutoResponseDto;
import com.example.auto.model.Auto;
import com.example.auto.repository.AutoRepository;
import com.example.auto.service.AutoService;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AutoServiceImpl implements AutoService {

    private final AutoRepository autoRepository;

    @Autowired
    public AutoServiceImpl(AutoRepository autoRepository) {
        this.autoRepository = autoRepository;
    }

    @Override
    @Cacheable(value = "auto", key = "#auto.vinCode")
    public GetAutoResponseDto getAuto(AutoRequestDto auto) {
        Auto entity = autoRepository.findAutoByVinCode(auto.getVinCode())
                .orElseThrow(() -> new RuntimeException("Авто не найдено"));

        // Сразу преобразуем в конечный DTO
        return convertToResponseDto(entity);
    }

    private GetAutoResponseDto convertToResponseDto(Auto entity) {
        // Явно инициализируем коллекцию
        Hibernate.initialize(entity.getServiceCompanies());

        return new GetAutoResponseDto(
                entity.getBrand(),
                entity.getNameModel(),
                entity.getServiceCompanies().stream()
                        .map(sc -> new ServiceCompanyCacheDto(sc.getId(), sc.getNameServiceCompany(), sc.getDescription()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    @Transactional
    public PostAutoResponseDto postAuto(AutoRequestDto autoRequestDto) {
        if (autoRepository.findAutoByVinCode(autoRequestDto.getVinCode()).isEmpty()) {
            autoRepository.save(new Auto(
                    autoRequestDto.getModelName(),
                    autoRequestDto.getVinCode(),
                    autoRequestDto.getBrandName(),
                    new ArrayList<>()

            ));
            return new PostAutoResponseDto(true);
        }
        return new PostAutoResponseDto(false);
    }

    @Override
    public GetAutoResponseDto isAutoByVin(String vin) {
        Optional<Auto> autoByVinCode = autoRepository.findAutoByVinCode(vin);
        if (autoByVinCode.isPresent()) {
            return new GetAutoResponseDto(
                    autoByVinCode.get().getBrand(),
                    autoByVinCode.get().getNameModel(),
                    autoByVinCode.get().getServiceCompanies().stream()
                            .map(sc -> new ServiceCompanyCacheDto(sc.getId(), sc.getNameServiceCompany(), sc.getDescription()))
                            .collect(Collectors.toList())
            );
        }
        return null;

    }
}


