package com.anb.postgres.repository;

import com.anb.postgres.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByFirstNameAndLastNameAndDepartment(String firstName,String lastName ,String department);
    Optional<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}


