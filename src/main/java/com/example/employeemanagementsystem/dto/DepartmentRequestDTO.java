package com.example.employeemanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentRequestDTO {

    // Department name
    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Department name cannot exceed 100 characters")
    private String name;

    // Unique department code
    @NotBlank(message = "Department code is required")
    @Size(max = 10, message = "Department code cannot exceed 10 characters")
    private String code;

    // Optional department description
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}
