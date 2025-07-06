package com.example.auto.service;

import com.example.auto.dto.request.AutoRequestDto;
import com.example.auto.dto.response.GetAutoResponseDto;
import com.example.auto.dto.response.PostAutoResponseDto;


public interface AutoService {
    GetAutoResponseDto getAuto(AutoRequestDto autoRequestDto);

    PostAutoResponseDto postAuto(AutoRequestDto autoRequestDto);
}
