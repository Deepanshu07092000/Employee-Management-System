package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.EmployeePageResponseDTO;
import com.example.employeemanagementsystem.dto.EmployeeRequestDTO;
import com.example.employeemanagementsystem.dto.EmployeeResponseDTO;
import com.example.employeemanagementsystem.entity.Address;
import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.entity.Employee;
import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exception.AddressNotFoundException;
import com.example.employeemanagementsystem.exception.DepartmentNotFoundException;
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
    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {

        log.info("Creating employee with email: {}", requestDTO.getEmail());

        // Find the department provided in the request
        Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + requestDTO.getDepartmentId()
                ));

        // Find the address provided in the request
        Address address = addressRepository.findById(requestDTO.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException(
                        "Address not found with id: " + requestDTO.getAddressId()
                ));

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

        // Set employee relationships
        employee.setDepartment(department);
        employee.setAddress(address);

        Employee savedEmployee = employeeRepository.save(employee);

        log.info("Employee created successfully with ID: {}", savedEmployee.getId());

        // Convert saved entity into response DTO
        return new EmployeeResponseDTO(
                savedEmployee.getId(),
                savedEmployee.getFirstName(),
                savedEmployee.getLastName(),
                savedEmployee.getEmail(),
                savedEmployee.getPhoneNumber(),
                savedEmployee.getDesignation(),
                savedEmployee.getSalary(),
                savedEmployee.getJoiningDate(),
                savedEmployee.getDepartment().getId(),
                savedEmployee.getDepartment().getName(),
                savedEmployee.getAddress().getId(),
                savedEmployee.getActive()
        );
    }

    // Retrieves an employee by ID
    // Retrieves an employee by ID along with department and address
    @Override
    public Employee getEmployeeById(Long id) {

        log.info("Fetching employee with ID: {}", id);

        return employeeRepository
                .findEmployeeWithDepartmentAndAddressById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found with ID: {}", id);
                    return new EmployeeNotFoundException("Employee not found with id: " + id);
                });
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
    public Employee updateEmployee(Long id, Employee employee) {

        log.info("Updating employee with ID: {}", id);

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found for update. ID: {}", id);
                    return new EmployeeNotFoundException("Employee not found with id: " + id);
                });

        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setPhoneNumber(employee.getPhoneNumber());
        existingEmployee.setDesignation(employee.getDesignation());
        existingEmployee.setSalary(employee.getSalary());
        existingEmployee.setJoiningDate(employee.getJoiningDate());
        existingEmployee.setActive(employee.getActive());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        log.info("Employee updated successfully with ID: {}", id);
        return updatedEmployee;
    }

    // Deletes an employee by ID
    @Override
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
}
