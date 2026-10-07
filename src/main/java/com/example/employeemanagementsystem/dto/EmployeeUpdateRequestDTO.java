package com.example.employeemanagementsystem.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class EmployeeUpdateRequestDTO {

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must contain exactly 10 digits")
    private String phoneNumber;

    @NotBlank
    @Size(max = 100)
    private String designation;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal salary;

    @NotNull
    private LocalDate joiningDate;

    @NotNull
    private Long departmentId;

    @NotNull
    private Long addressId;

    @NotNull
    private Boolean active;
}
