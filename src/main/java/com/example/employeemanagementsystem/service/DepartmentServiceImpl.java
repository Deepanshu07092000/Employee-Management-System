package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.DepartmentRequestDTO;
import com.example.employeemanagementsystem.dto.DepartmentResponseDTO;
import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService{

    private final DepartmentRepository departmentRepository;

    // Creates a new department
    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO) {

        log.info("Creating department with code: {}", requestDTO.getCode());

        // Convert request DTO into entity
        Department department = new Department();

        department.setName(requestDTO.getName());
        department.setCode(requestDTO.getCode());
        department.setDescription(requestDTO.getDescription());

        // Save department
        Department savedDepartment = departmentRepository.save(department);

        log.info(
                "Department created successfully with ID: {}",
                savedDepartment.getId()
        );

        // Convert entity into response DTO
        return new DepartmentResponseDTO(
                savedDepartment.getId(),
                savedDepartment.getName(),
                savedDepartment.getCode(),
                savedDepartment.getDescription()
        );
    }
}
