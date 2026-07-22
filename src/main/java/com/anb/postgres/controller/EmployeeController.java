package com.anb.postgres.controller;

import com.anb.postgres.dto.EmployeeResponse;
import com.anb.postgres.entity.Employee;
import com.anb.postgres.services.EmployeeService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("")
    public ResponseEntity<List<Employee>> getAll(){
        return ResponseEntity.ok(employeeService.findAll());
    }
    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId){
        return ResponseEntity.ok(employeeService.findById(employeeId));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Employee> deleteEmployeeById(@PathVariable Long employeeId) {
        employeeService.deleteById(employeeId);
       return ResponseEntity.noContent().build();
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable Long employeeId, @RequestBody Employee emp) {
        Employee updatedEmployee = employeeService.updateById(employeeId, emp);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PostMapping("")
    public ResponseEntity<EmployeeResponse> addEmployee(@RequestBody Employee emp){
       EmployeeResponse response  =  employeeService.addEmployee(emp);
       return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

