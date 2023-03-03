package com.tutorial.pdfGeneration.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="emp_id")
    private Integer empId;
    @Column(name="emp_Name")
    private String empName;
    @Column(name="emp_Sal")
    private String empSal;
    @Column(name="emp_Dept")
    private String empDept;
}
