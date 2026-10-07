package com.example.employeemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    //Department  1 ───────── * Employee
    //Employee    1 ───────── 1 Address
    //Employee    * ───────── * Project

    // Primary key of the employee table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true,length = 50)
    private String email;

    @Column(nullable = false, unique = true,length = 10)
    private String phoneNumber;

    @Column(nullable = false, length = 100)
    private String designation;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(nullable = false)
    private LocalDate joiningDate;

    // Stores employee profile image as a large binary object
    @Lob
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    // Stores the MIME type of the uploaded image
    @Column(name = "profile_image_content_type", length = 100)
    private String profileImageContentType;

    // Whether the employee is currently active
    @Column(nullable = false)
    private Boolean active;

    // Each employee belongs to one department (many-to-one)
    @ManyToOne(fetch = FetchType.LAZY)  // department won't automatically load whenever we fetch an employee
    @JoinColumn(name = "department_id", nullable = false)  // creates the FK column in the employees table
    private Department department;

    // Each employee has one address
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false, unique = true)
    private Address address;

    // An employee can work on multiple projects
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_projects",

            // FK referring to employees table
            joinColumns = @JoinColumn(name = "employee_id"),

            // FK referring to projects table
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects;

}
