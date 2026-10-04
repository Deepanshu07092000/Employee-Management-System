package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.dto.AddressRequestDTO;
import com.example.employeemanagementsystem.dto.AddressResponseDTO;
import com.example.employeemanagementsystem.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final AddressService addressService;

    // Creates a new address
    @PostMapping
    public ResponseEntity<AddressResponseDTO> createAddress(
            @Valid @RequestBody AddressRequestDTO requestDTO) {

        log.info(
                "POST /api/addresses - Creating address for city: {}",
                requestDTO.getCity()
        );

        AddressResponseDTO response = addressService.createAddress(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
