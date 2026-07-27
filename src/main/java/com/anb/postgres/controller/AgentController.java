package com.anb.postgres.controller;

import com.anb.postgres.controller.AgentOrchestrator;
import com.anb.postgres.dto.AgentRequest;
import com.anb.postgres.dto.EmployeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcp/agent")
public class AgentController {

    @Autowired
    private AgentOrchestrator orchestrator;

    @PostMapping("/add-employee")
    public ResponseEntity<EmployeeResponse> runAddEmployee(@RequestBody AgentRequest req) throws Exception{
        var resp = orchestrator.orchestrateAddEmployee(req.getSkillName(), req.getVariables());
        return ResponseEntity.ok(resp);
    }
}
