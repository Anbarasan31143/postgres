package com.anb.postgres.services;

import com.anb.postgres.entity.Employee;

public interface EmployeeService {

    Employee findById(Long Id);
    void deleteById(Long Id);
    Employee addEmployee(Employee emp);
}
