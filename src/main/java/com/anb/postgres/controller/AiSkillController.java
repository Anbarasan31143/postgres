package com.anb.postgres.controller;

import com.anb.postgres.dto.EmployeeResponse;
import com.anb.postgres.entity.Employee;
import com.anb.postgres.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcp/skill")
public class AiSkillController {

    private static final Logger log = LoggerFactory.getLogger(AiSkillController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/add-employee")
    public ResponseEntity<EmployeeResponse> addEmployeeSkill(@RequestBody Employee employee){
        log.info("Received MCP skill call to add employee: {} {}", employee.getFirstName(), employee.getLastName());
        EmployeeResponse resp = employeeService.addEmployee(employee);
        return ResponseEntity.ok(resp);
    }
}
