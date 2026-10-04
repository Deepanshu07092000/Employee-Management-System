package com.example.employeemanagementsystem.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class EmployeeRequestDTO {

    // Employee first name
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    // Employee last name
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    // Employee email address
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    private String email;

    // Employee phone number
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain exactly 10 digits"
    )
    private String phoneNumber;

    // Employee designation
    @NotBlank(message = "Designation is required")
    @Size(max = 100, message = "Designation cannot exceed 100 characters")
    private String designation;

    // Employee salary
    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than zero")
    private BigDecimal salary;

    // Employee joining date
    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    // Department ID assigned to the employee
    @NotNull(message = "Department ID is required")
    private Long departmentId;

    // Address ID assigned to the employee
    @NotNull(message = "Address ID is required")
    private Long addressId;

    // Indicates whether the employee is active
    @NotNull(message = "Active status is required")
    private Boolean active;
}
