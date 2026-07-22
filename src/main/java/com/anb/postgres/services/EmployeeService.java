package com.anb.postgres.services;

import com.anb.postgres.dto.EmployeeResponse;
import com.anb.postgres.entity.Employee;

import java.util.List;

public interface EmployeeService {



    Employee findById(Long Id);
    List<Employee> findAll();
    void deleteById(Long Id);
    Employee updateById(Long Id, Employee emp);
    EmployeeResponse addEmployee(Employee emp);
}
