package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.*;
import org.springframework.web.multipart.MultipartFile;


public interface EmployeeService {

    // Creates a new employee
    EmployeeResponseDTO createEmployee(EmployeeMultipartRequestDTO requestDTO, MultipartFile profileImage
    );

    // Retrieves an employee by its ID
    EmployeeResponseDTO getEmployeeById(Long id);

    // Retrieves employees with pagination, sorting and searching
    EmployeePageResponseDTO getEmployees(
            int page,
            int size,
            String sortBy,
            String sortDirection,
            String search
    );

    // Updates an existing employee
    EmployeeResponseDTO updateEmployee(Long id, EmployeeUpdateRequestDTO requestDTO);

    // Deletes an employee by its ID
    void deleteEmployee(Long id);

    // Retrieves employee profile image from database
    byte[] getEmployeeImage(Long id);

    // Retrieves employee profile image content type
    String getEmployeeImageContentType(Long id);
}
