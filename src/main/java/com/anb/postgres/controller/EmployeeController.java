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
    @GetMapping("/{Id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long Id){
        return ResponseEntity.ok(employeeService.findById(Id));
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<Employee> deleteEmployeeById(@PathVariable Long Id) {
        employeeService.deleteById(Id);
       return ResponseEntity.noContent().build();
    }

    @PutMapping("/{Id}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable Long Id, @RequestBody Employee emp) {
        Employee updatedEmployee = employeeService.updateById(Id, emp);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PostMapping("")
    public ResponseEntity<EmployeeResponse> addEmployee(@RequestBody Employee emp){
       EmployeeResponse response  =  employeeService.addEmployee(emp);
       return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

