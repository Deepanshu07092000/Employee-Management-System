package com.example.employeemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressResponseDTO {

    // Address primary key
    private Long id;

    // Street or house information
    private String street;

    // City
    private String city;

    // State
    private String state;

    // Country
    private String country;

    // Postal/PIN code
    private String postalCode;
}
