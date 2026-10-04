package com.example.employeemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DepartmentResponseDTO {

    // Department primary key
    private Long id;

    // Department name
    private String name;

    // Unique department code
    private String code;

    // Department description
    private String description;
}
