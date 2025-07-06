package com.example.auto.service.impl;

import com.example.auto.dto.request.AutoRequestDto;
import com.example.auto.dto.response.GetAutoResponseDto;
import com.example.auto.dto.response.PostAutoResponseDto;
import com.example.auto.model.Auto;
import com.example.auto.repository.AutoRepository;
import com.example.auto.service.AutoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AutoServiceImpl implements AutoService {

    private final AutoRepository autoRepository;

    @Autowired
    public AutoServiceImpl(AutoRepository autoRepository) {
        this.autoRepository = autoRepository;
    }

    @Override
    public GetAutoResponseDto getAuto(AutoRequestDto auto) {
        if (!autoRepository.findAutoByVinCode(auto.getVinCode()).isEmpty()) {
            Auto resAuto = autoRepository.findAutoByVinCode(auto.getVinCode()).get();
            return new GetAutoResponseDto(resAuto.getBrand(), resAuto.getNameModel(), resAuto.getServiceCompanies());
        }
        throw new RuntimeException("Авто не найдено");
    }

    @Override
    @Transactional
    public PostAutoResponseDto postAuto(AutoRequestDto autoRequestDto) {
        if (autoRepository.findAutoByVinCode(autoRequestDto.getVinCode()) == null) {
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
}
