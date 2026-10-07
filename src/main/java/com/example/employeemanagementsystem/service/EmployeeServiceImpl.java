package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.*;
import com.example.employeemanagementsystem.entity.Address;
import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.entity.Employee;
import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exception.AddressNotFoundException;
import com.example.employeemanagementsystem.exception.DepartmentNotFoundException;
import com.example.employeemanagementsystem.exception.ProfileImageNotFoundException;
import com.example.employeemanagementsystem.mapper.EmployeeMapper;
import com.example.employeemanagementsystem.repository.AddressRepository;
import com.example.employeemanagementsystem.repository.DepartmentRepository;
import com.example.employeemanagementsystem.repository.EmployeeRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final AddressRepository addressRepository;
    private final EmployeeMapper employeeMapper;

    // Creates a new employee
    @Transactional
    @Override
    public EmployeeResponseDTO createEmployee(EmployeeMultipartRequestDTO requestDTO, MultipartFile profileImage) {
        log.info("Creating employee with email: {}", requestDTO.getEmail());

        // Validate profile image
        if (profileImage == null || profileImage.isEmpty()) {
            throw new IllegalArgumentException("Profile image is required");
        }

        // Find the department provided in the request
        Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + requestDTO.getDepartmentId()));

        // Find the address provided in the request
        Address address = addressRepository.findById(requestDTO.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + requestDTO.getAddressId()));

        // Convert request DTO into Employee entity
        Employee employee = new Employee();

        employee.setFirstName(requestDTO.getFirstName());
        employee.setLastName(requestDTO.getLastName());
        employee.setEmail(requestDTO.getEmail());
        employee.setPhoneNumber(requestDTO.getPhoneNumber());
        employee.setDesignation(requestDTO.getDesignation());
        employee.setSalary(requestDTO.getSalary());
        employee.setJoiningDate(requestDTO.getJoiningDate());
        employee.setActive(requestDTO.getActive());

        try {
            // Store image bytes in database
            employee.setProfileImage(profileImage.getBytes());

            // Store image MIME type such as image/jpeg or image/png
            employee.setProfileImageContentType(profileImage.getContentType());

        } catch (IOException exception) {
            log.error("Failed to read profile image", exception);
            throw new RuntimeException("Failed to process profile image");
        }

        // Set employee relationships
        employee.setDepartment(department);
        employee.setAddress(address);
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());

        // Convert saved entity into response DTO
        return employeeMapper.toResponseDTO(savedEmployee);
    }

    // Retrieves an employee by ID along with department and address
    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        log.info("Fetching employee with ID: {}", id);
        Employee employee = employeeRepository
                .findEmployeeWithDepartmentAndAddressById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found with ID: {}", id);
                    return new EmployeeNotFoundException("Employee not found with id: " + id);
                });

        // Convert entity to response DTO
        return employeeMapper.toResponseDTO(employee);
    }

    // Retrieves employees with pagination, sorting and searching
    @Transactional(readOnly = true)
    @Override
    public EmployeePageResponseDTO getEmployees(
            int page,
            int size,
            String sortBy,
            String sortDirection,
            String search) {

        log.info(
                "Fetching employees - page: {}, size: {}, sortBy: {}, sortDirection: {}, search: {}",
                page, size, sortBy, sortDirection, search
        );

        // Determine sorting direction
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Create pagination and sorting information
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );

        Page<Employee> employeePage;

        // Search only when a search value is provided
        if (search != null && !search.trim().isEmpty()) {

            employeePage = employeeRepository
                            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDesignationContainingIgnoreCase(
                                    search,
                                    search,
                                    search,
                                    search,
                                    pageable
                            );

        } else {

            // Fetch all employees with pagination and sorting
            employeePage = employeeRepository.findAllWithDepartmentAndAddress(pageable);
        }

        // Convert Employee entities into response DTOs
        List<EmployeeResponseDTO> employees = employeePage
                .getContent()
                .stream()
                .map(employeeMapper::toResponseDTO)
                .toList();

        log.info(
                "Employees fetched successfully. Total elements: {}",
                employeePage.getTotalElements()
        );

        // Build paginated response
        return new EmployeePageResponseDTO(
                employees,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.isFirst(),
                employeePage.isLast()
        );
    }

    // Updates an existing employee
    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeUpdateRequestDTO requestDTO) {

        log.info("Updating employee with ID: {}", id);

        // Find the existing employee
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found for update. ID: {}", id);
                    return new EmployeeNotFoundException("Employee not found with id: " + id);
                });

        // Find the new department
        Department department = departmentRepository
                .findById(requestDTO.getDepartmentId())
                .orElseThrow(() -> {
                    log.warn(
                            "Department not found. ID: {}",
                            requestDTO.getDepartmentId()
                    );
                    return new DepartmentNotFoundException(
                            "Department not found with id: "
                                    + requestDTO.getDepartmentId()
                    );
                });

        // Find the new address
        Address address = addressRepository
                .findById(requestDTO.getAddressId())
                .orElseThrow(() -> {
                    log.warn(
                            "Address not found. ID: {}",
                            requestDTO.getAddressId()
                    );
                    return new AddressNotFoundException("Address not found with id: " + requestDTO.getAddressId());
                });

        // Update employee fields
        existingEmployee.setFirstName(requestDTO.getFirstName());
        existingEmployee.setLastName(requestDTO.getLastName());
        existingEmployee.setEmail(requestDTO.getEmail());
        existingEmployee.setPhoneNumber(requestDTO.getPhoneNumber());
        existingEmployee.setDesignation(requestDTO.getDesignation());
        existingEmployee.setSalary(requestDTO.getSalary());
        existingEmployee.setJoiningDate(requestDTO.getJoiningDate());
        existingEmployee.setActive(requestDTO.getActive());

        // Update relationships
        existingEmployee.setDepartment(department);
        existingEmployee.setAddress(address);

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        log.info("Employee updated successfully with ID: {}", id);

        // Convert entity into response DTO
        return employeeMapper.toResponseDTO(updatedEmployee);
    }

    // Deletes an employee by ID
    @Override
    @Transactional
    public void deleteEmployee(Long id) {

        log.info("Deleting employee with ID: {}", id);
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found for deletion. ID: {}", id);
                    return new EmployeeNotFoundException(
                            "Employee not found with id: " + id
                    );
                });

        employeeRepository.delete(existingEmployee);
        log.info("Employee deleted successfully with ID: {}", id);
    }

    // Retrieves employee profile image from database
    @Override
    @Transactional(readOnly = true)
    public byte[] getEmployeeImage(Long id) {

        log.info("Fetching profile image for employee ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found with ID: {}", id);
                    return new EmployeeNotFoundException("Employee not found with id: " + id);
                });

        // Check whether employee has an image
        if (employee.getProfileImage() == null || employee.getProfileImage().length == 0) {

            log.warn("Profile image not found for employee ID: {}", id);
            throw new ProfileImageNotFoundException("Profile image not found for employee with id: " + id);
        }
        return employee.getProfileImage();
    }

    // Retrieves the MIME type of employee profile image
    @Override
    @Transactional(readOnly = true)
    public String getEmployeeImageContentType(Long id) {

        log.info("Fetching profile image content type for employee ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found with ID: {}", id);
                    return new EmployeeNotFoundException("Employee not found with id: " + id);
                });

        // Check whether image content type exists
        if (employee.getProfileImageContentType() == null || employee.getProfileImageContentType().isBlank()) {
            log.warn("Profile image content type not found for employee ID: {}", id);
            throw new ProfileImageNotFoundException("Profile image not found for employee with id: " + id);
        }
        return employee.getProfileImageContentType();
    }
}
