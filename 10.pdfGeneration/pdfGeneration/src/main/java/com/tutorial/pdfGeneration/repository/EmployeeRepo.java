package com.tutorial.pdfGeneration.repository;

import com.tutorial.pdfGeneration.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee,Integer>, JpaSpecificationExecutor<Employee> {

    List<Employee> findAll();
}
