package com.example.auto.controller;

import com.example.auto.dto.request.AutoRequestDto;
import com.example.auto.dto.response.GetAutoResponseDto;
import com.example.auto.dto.response.PostAutoResponseDto;
import com.example.auto.service.AutoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/v1/auto")
public class AutoController {

    private final AutoService autoService;

    @Autowired
    public AutoController(AutoService autoService) {
        this.autoService = autoService;
    }

    @GetMapping("/")

    public ResponseEntity<GetAutoResponseDto> getAuto(@RequestParam String nameModel,
                                                      @RequestParam String vinCode,
                                                      @RequestParam String brandName) {
        return ResponseEntity.ok(autoService.getAuto(new AutoRequestDto(nameModel, vinCode, brandName)));
    }

    @PostMapping("/")
    public ResponseEntity<PostAutoResponseDto> postAuto(@RequestParam String nameModel,
                                                        @RequestParam String vinCode,
                                                        @RequestParam String brandName) {
        return ResponseEntity.ok(autoService.postAuto(new AutoRequestDto(nameModel, vinCode, brandName)));
    }
}
