package com.example.employeemanagementsystem.exception;

public class EmployeeNotFoundException extends RuntimeException {

    // Thrown when the requested employee does not exist
    public EmployeeNotFoundException(String message) {
    }
}
