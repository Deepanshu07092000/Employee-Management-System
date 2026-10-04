package com.example.employeemanagementsystem.repository;

import com.example.employeemanagementsystem.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Searches employees by name, email or designation
    Page<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDesignationContainingIgnoreCase(
            String firstName,
            String lastName,
            String email,
            String designation,
            Pageable pageable
    );

    // Fetches employee along with department and address by ID
    @Query("""
        SELECT e
        FROM Employee e
        JOIN FETCH e.department
        JOIN FETCH e.address
        WHERE e.id = :id
        """)
    Optional<Employee> findEmployeeWithDepartmentAndAddressById(@Param("id") Long id);

    // Fetches employees along with department and address for pagination
    @Query("""
        SELECT e
        FROM Employee e
        JOIN FETCH e.department
        JOIN FETCH e.address
        """)
    Page<Employee> findAllWithDepartmentAndAddress(Pageable pageable);
}
