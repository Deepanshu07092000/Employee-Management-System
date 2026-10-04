package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.AddressRequestDTO;
import com.example.employeemanagementsystem.dto.AddressResponseDTO;

public interface AddressService {

    // Creates a new address
    AddressResponseDTO createAddress(AddressRequestDTO requestDTO);
}
