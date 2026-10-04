package com.example.employeemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Project name
    @Column(nullable = false, unique = true, length = 150)
    private String name;

    // Short description of the project
    @Column(length = 500)
    private String description;

    // Project start date
    @Column(nullable = false)
    private LocalDate startDate;

    // Project end date
    private LocalDate endDate;

    // Whether the project is currently active
    @Column(nullable = false)
    private Boolean active;

    // A project can have multiple employees
    @ManyToMany(mappedBy = "projects", fetch = FetchType.LAZY)
    private Set<Employee> employees;
}
