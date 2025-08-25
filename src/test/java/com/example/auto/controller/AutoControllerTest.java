package com.example.auto.controller;

import com.example.auto.dto.request.AutoRequestDto;
import com.example.auto.dto.response.GetAutoResponseDto;
import com.example.auto.dto.response.PostAutoResponseDto;
import com.example.auto.service.AutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AutoControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAuto_ShouldReturnAuto() {
        // Arrange
        String url = "/api/v1/auto/?nameModel=ModelX&vinCode=VIN123&brandName=BrandZ";

        // Act
        ResponseEntity<GetAutoResponseDto> response = restTemplate.getForEntity(
                url, GetAutoResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Добавьте дополнительные проверки для response.getBody()
    }

    @Test
    void getAutoToVin_ShouldReturnAutoByVin() {
        // Arrange
        String vin = "VIN123";
        String url = "/api/v1/auto/" + vin;

        // Act
        ResponseEntity<GetAutoResponseDto> response = restTemplate.getForEntity(
                url, GetAutoResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Добавьте дополнительные проверки для response.getBody()
    }
}
