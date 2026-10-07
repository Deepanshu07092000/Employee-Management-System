package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.dto.EmployeePageResponseDTO;
import com.example.employeemanagementsystem.dto.EmployeeResponseDTO;
import com.example.employeemanagementsystem.dto.EmployeeUpdateRequestDTO;
import com.example.employeemanagementsystem.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.employeemanagementsystem.dto.EmployeeMultipartRequestDTO;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Slf4j
@Validated // "Apply validation to the parameters of this controller's methods."
public class EmployeeController {

    private final EmployeeService employeeService;

    // Retrieves employee with pagination, sorting and searching
    // Get first 10 employees - GET http://localhost:8080/api/employees
    // Get page 2 - GET http://localhost:8080/api/employees?page=1&size=10
    // Sort by salary - GET http://localhost:8080/api/employees?sortBy=salary&sortDirection=desc
    // Search - GET http://localhost:8080/api/employees?search=john
    // Combine everything - GET http://localhost:8080/api/employees?page=0&size=5&search=john&sortBy=salary&sortDirection=desc

    @GetMapping
    public ResponseEntity<EmployeePageResponseDTO> getEmployees(@RequestParam(defaultValue = "0")  @Min(value = 0, message = "Page number cannot be negative")int page,
                                                                @RequestParam(defaultValue = "10")  @Min(value = 1, message = "Page size must be at least 1") @Max(value = 100, message = "Page size cannot exceed 100")int size,
                                                                @RequestParam(defaultValue = "id") String sortBy,
                                                                @RequestParam(defaultValue = "asc") String sortDirection,
                                                                @RequestParam(required = false)
    String search){
        log.info(
                "GET /api/employees - page: {}, size: {}, sortBy: {}, sortDirection: {}, search: {}",
                page, size, sortBy, sortDirection, search
        );

        EmployeePageResponseDTO response = employeeService.getEmployees(
                        page,
                        size,
                        sortBy,
                        sortDirection,
                        search
                );

        return ResponseEntity.ok(response);
    }

    // Creates an employee along with a profile image
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @ModelAttribute EmployeeMultipartRequestDTO requestDTO, @RequestParam("profileImage") MultipartFile profileImage) {

        log.info(
                "Received request to create employee with email: {}",
                requestDTO.getEmail()
        );

        EmployeeResponseDTO response = employeeService.createEmployee(requestDTO, profileImage);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Retrieves a single employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
        log.info("Fetching employee with ID: {}", id);
        EmployeeResponseDTO response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(response);
    }

    // Updates an existing employee
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateRequestDTO requestDTO) {

        log.info("Received request to update employee with ID: {}", id);
        EmployeeResponseDTO response = employeeService.updateEmployee(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    // delete an existing employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.info("Received request to delete employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    // Retrieves employee profile image
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable Long id) {

        log.info("Fetching profile image for employee ID: {}", id);
        byte[] image = employeeService.getEmployeeImage(id);
        String contentType = employeeService.getEmployeeImageContentType(id);

        // Return image with its correct MIME type
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(image);
    }
}
