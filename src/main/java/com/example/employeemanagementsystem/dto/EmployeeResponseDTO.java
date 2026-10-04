package com.example.employeemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

    // Employee primary key
    private Long id;

    // Employee first name
    private String firstName;

    // Employee last name
    private String lastName;

    // Employee email address
    private String email;

    // Employee phone number
    private String phoneNumber;

    // Employee designation
    private String designation;

    // Employee salary
    private BigDecimal salary;

    // Employee joining date
    private LocalDate joiningDate;

    // Department information
    private Long departmentId;
    private String departmentName;

    // Address information
    private Long addressId;

    // Employee active status
    private Boolean active;
}
