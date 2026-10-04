package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.DepartmentRequestDTO;
import com.example.employeemanagementsystem.dto.DepartmentResponseDTO;

public interface DepartmentService {

    // Creates a new department
    DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO);


}
