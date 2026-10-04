package com.example.employeemanagementsystem.repository;

import com.example.employeemanagementsystem.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

// Provides CRUD and database operations for Address
public interface AddressRepository extends JpaRepository<Address, Long> {
}