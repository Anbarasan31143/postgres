package com.anb.postgres.services;

import com.anb.postgres.dto.EmployeeResponse;
import com.anb.postgres.entity.Employee;

import java.util.List;

public interface EmployeeService {

    Employee findById(Long employeeId);
    Employee searchByName(String name);
    List<Employee> findAll();
    void deleteById(Long employeeId);
    Employee updateById(Long employeeId, Employee emp);
    EmployeeResponse addEmployee(Employee emp);
}
