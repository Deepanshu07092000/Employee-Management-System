package com.example.employeemanagementsystem.mapper;

import com.example.employeemanagementsystem.dto.EmployeeResponseDTO;
import com.example.employeemanagementsystem.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    // Converts Employee entity into response DTO
    public EmployeeResponseDTO toResponseDTO(Employee employee) {

        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getDesignation(),
                employee.getSalary(),
                employee.getJoiningDate(),
                employee.getDepartment().getId(),
                employee.getDepartment().getName(),
                employee.getAddress().getId(),
                employee.getActive()
        );
    }
}
