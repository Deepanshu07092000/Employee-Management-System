package com.example.employeemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class EmployeePageResponseDTO {

    // Employees for the current page
    private List<EmployeeResponseDTO> employees;

    // Current page number
    private int pageNumber;

    // Number of employees per page
    private int pageSize;

    // Total number of employees
    private long totalElements;

    // Total number of pages
    private int totalPages;

    // Whether this is the first page
    private boolean first;

    // Whether this is the last page
    private boolean last;
}
