package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.EmployeePageResponseDTO;
import com.example.employeemanagementsystem.dto.EmployeeRequestDTO;
import com.example.employeemanagementsystem.dto.EmployeeResponseDTO;
import com.example.employeemanagementsystem.entity.Employee;


public interface EmployeeService {

    // Creates a new employee
    EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO);

    // Retrieves an employee by its ID
    Employee getEmployeeById(Long id);

    // Retrieves employees with pagination, sorting and searching
    EmployeePageResponseDTO getEmployees(
            int page,
            int size,
            String sortBy,
            String sortDirection,
            String search
    );

    // Updates an existing employee
    Employee updateEmployee(Long id, Employee employee);

    // Deletes an employee by its ID
    void deleteEmployee(Long id);
}
