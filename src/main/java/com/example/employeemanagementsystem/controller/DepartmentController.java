package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.dto.DepartmentRequestDTO;
import com.example.employeemanagementsystem.dto.DepartmentResponseDTO;
import com.example.employeemanagementsystem.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    // Creates a new department
    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(
            @Valid @RequestBody DepartmentRequestDTO requestDTO) {

        log.info(
                "POST /api/departments - Creating department with code: {}",
                requestDTO.getCode()
        );

        DepartmentResponseDTO response =
                departmentService.createDepartment(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
