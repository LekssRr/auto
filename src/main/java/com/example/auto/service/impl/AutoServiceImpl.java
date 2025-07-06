package com.example.auto.service.impl;

import com.example.auto.dto.request.AutoRequestDto;
import com.example.auto.dto.response.GetAutoResponseDto;
import com.example.auto.dto.response.PostAutoResponseDto;
import com.example.auto.model.Auto;
import com.example.auto.repository.AutoRepository;
import com.example.auto.service.AutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutoServiceImpl implements AutoService {

    private final AutoRepository autoRepository;

    @Autowired
    public AutoServiceImpl(AutoRepository autoRepository) {
        this.autoRepository = autoRepository;
    }

    @Override
    public GetAutoResponseDto getAuto(AutoRequestDto auto) {
        return null;
    }

    @Override
    public PostAutoResponseDto postAuto(AutoRequestDto autoRequestDto) {
        return null;
    }
}
