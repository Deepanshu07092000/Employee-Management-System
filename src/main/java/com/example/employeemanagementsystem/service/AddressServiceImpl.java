package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.AddressRequestDTO;
import com.example.employeemanagementsystem.dto.AddressResponseDTO;
import com.example.employeemanagementsystem.entity.Address;
import com.example.employeemanagementsystem.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    // Creates a new address
    @Override
    public AddressResponseDTO createAddress(AddressRequestDTO requestDTO) {

        log.info("Creating address for city: {}", requestDTO.getCity());

        // Convert request DTO into entity
        Address address = new Address();

        address.setStreet(requestDTO.getStreet());
        address.setCity(requestDTO.getCity());
        address.setState(requestDTO.getState());
        address.setCountry(requestDTO.getCountry());
        address.setPostalCode(requestDTO.getPostalCode());

        // Save address
        Address savedAddress = addressRepository.save(address);

        log.info(
                "Address created successfully with ID: {}",
                savedAddress.getId()
        );

        // Convert entity into response DTO
        return new AddressResponseDTO(
                savedAddress.getId(),
                savedAddress.getStreet(),
                savedAddress.getCity(),
                savedAddress.getState(),
                savedAddress.getCountry(),
                savedAddress.getPostalCode()
        );
    }
}
