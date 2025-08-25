package com.example.auto.service;

import com.example.auto.dto.request.AutoRequestDto;
import com.example.auto.dto.response.GetAutoResponseDto;
import com.example.auto.dto.response.PostAutoResponseDto;
import com.example.auto.model.Auto;
import com.example.auto.model.ServiceCompany;
import com.example.auto.repository.AutoRepository;
import com.example.auto.service.impl.AutoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AutoServiceImplTest {
    @Mock
    private AutoRepository autoRepository;

    @InjectMocks
    private AutoServiceImpl autoService;

    @Test
    void getAuto_ShouldReturnAuto_WhenExists() {
        // Arrange
        AutoRequestDto request = new AutoRequestDto("ModelX", "VIN123", "BrandZ");
        Auto autoEntity = createTestAuto();

        when(autoRepository.findAutoByVinCode("VIN123")).thenReturn(Optional.of(autoEntity));

        // Act
        GetAutoResponseDto result = autoService.getAuto(request);

        // Assert
        assertNotNull(result);
        assertEquals("BrandZ", result.getBrandName());
        assertEquals("ModelX", result.getModelName());
    }
    @Test
    void getAuto_ShouldThrowException_WhenNotExists() {
        // Arrange
        AutoRequestDto request = new AutoRequestDto("ModelX", "VIN123", "BrandZ");

        when(autoRepository.findAutoByVinCode("VIN123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> autoService.getAuto(request));
    }
    @Test
    void postAuto_ShouldReturnTrue_WhenNewAuto() {
        // Arrange
        AutoRequestDto request = new AutoRequestDto("ModelX", "VIN123", "BrandZ");

        when(autoRepository.findAutoByVinCode("VIN123")).thenReturn(Optional.empty());

        // Act
        PostAutoResponseDto result = autoService.postAuto(request);

        verify(autoRepository, times(1)).save(any(Auto.class));
    }
    @Test
    void postAuto_ShouldReturnFalse_WhenAutoExists() {
        // Arrange
        AutoRequestDto request = new AutoRequestDto("ModelX", "VIN123", "BrandZ");
        Auto autoEntity = createTestAuto();

        when(autoRepository.findAutoByVinCode("VIN123")).thenReturn(Optional.of(autoEntity));

        // Act
        PostAutoResponseDto result = autoService.postAuto(request);

        verify(autoRepository, never()).save(any(Auto.class));
    }
    @Test
    void isAutoByVin_ShouldReturnAuto_WhenExists() {
        // Arrange
        String vin = "VIN123";
        Auto autoEntity = createTestAuto();

        when(autoRepository.findAutoByVinCode(vin)).thenReturn(Optional.of(autoEntity));

        // Act
        GetAutoResponseDto result = autoService.isAutoByVin(vin);

        // Assert
        assertNotNull(result);
        assertEquals("BrandZ", result.getBrandName());
    }
    @Test
    void isAutoByVin_ShouldReturnNull_WhenNotExists() {
        // Arrange
        String vin = "VIN123";

        when(autoRepository.findAutoByVinCode(vin)).thenReturn(Optional.empty());

        // Act
        GetAutoResponseDto result = autoService.isAutoByVin(vin);

        // Assert
        assertNull(result);
    }

    private Auto createTestAuto() {
        Auto auto = new Auto("ModelX", "VIN123", "BrandZ", new ArrayList<>());

        // Добавляем тестовые сервисные компании
        ServiceCompany sc1 = new ServiceCompany();
        sc1.setNameServiceCompany("Service 1");
        auto.getServiceCompanies().add(sc1);

        return auto;
    }
}
