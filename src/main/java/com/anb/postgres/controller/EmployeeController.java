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

    @GetMapping("get/{Id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long Id){
        return ResponseEntity.ok(employeeService.findById(Id));
    }

    @DeleteMapping("delete/{Id}")
    public ResponseEntity<Employee> deleteEmployeeById(@PathVariable Long Id) {
        employeeService.deleteById(Id);
       return ResponseEntity.noContent().build();
    }

    @PostMapping("/add")
    public ResponseEntity<EmployeeResponse> addEmployee(@RequestBody Employee emp){
       EmployeeResponse response  =  employeeService.addEmployee(emp);
       return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

