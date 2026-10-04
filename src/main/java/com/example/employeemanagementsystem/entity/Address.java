package com.example.employeemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // House number or street information
    @Column(nullable = false, length = 200)
    private String street;

    // City where the employee lives
    @Column(nullable = false, length = 100)
    private String city;

    // State where the employee lives
    @Column(nullable = false, length = 100)
    private String state;

    // Country where the employee lives
    @Column(nullable = false, length = 100)
    private String country;

    // Postal/PIN code
    @Column(nullable = false, length = 10)
    private String postalCode;

    // Each address belongs to one employee
    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
    private Employee employee;


}
