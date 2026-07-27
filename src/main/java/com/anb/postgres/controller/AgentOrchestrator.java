package com.anb.postgres.controller;

import com.anb.postgres.dto.EmployeeResponse;
import com.anb.postgres.entity.Employee;
import com.anb.postgres.services.EmployeeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AgentOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(AgentOrchestrator.class);

    @Value("${llm.provider:mock}")
    private String llmProvider;

    @Autowired
    private SkillLoader skillLoader;

    @Autowired(required = false)
    private LlmClient llmClient;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    public EmployeeResponse orchestrateAddEmployee(String skillName, Map<String,String> variables) throws Exception{
        String template = skillLoader.loadSkill(skillName);

        String filled = template;
        if(variables != null){
            for(var e : variables.entrySet()){
                filled = filled.replace("{{" + e.getKey() + "}}", e.getValue());
            }
        }

        // Always call LLM (whether mock or real) to parse and extract data
        String llmOutput;
        if(llmClient == null){
            // If no LLM client available, use filled template as-is (fallback)
            llmOutput = filled;
        } else {
            // Call LLM client to parse and extract employee data
            llmOutput = llmClient.generate(filled);
        }

        int start = llmOutput.indexOf('{');
        int end = llmOutput.lastIndexOf('}');
        if(start == -1 || end == -1 || end <= start) {
            throw new IllegalArgumentException("LLM output does not contain a JSON object");
        }
        String json = llmOutput.substring(start, end+1);

        Employee employee = objectMapper.readValue(json, Employee.class);
        log.info("Orchestrator parsed employee: {} {}", employee.getFirstName(), employee.getLastName());

        return employeeService.addEmployee(employee);
    }
    public Employee orchestrateUpdateEmployee(String skillName, Map<String, String> variables) throws Exception {
        String template = skillLoader.loadSkill(skillName);

        String filled = template;
        if (variables != null) {
            for (var e : variables.entrySet()) {
                log.info("Replacing placeholder: {} with value: {}", e.getKey(), e.getValue());
                filled = filled.replace("{{" + e.getKey() + "}}", e.getValue());
            }
        }

        // Always call LLM (whether mock or real) to parse and extract data
        String llmOutput;
        if (llmClient == null) {
            llmOutput = filled;
        } else {
            llmOutput = llmClient.generate(filled);
        }

        int start = llmOutput.indexOf('{');
        int end = llmOutput.lastIndexOf('}');
        if (start == -1 || end == -1 || end <= start) {
            throw new IllegalArgumentException("LLM output does not contain a JSON object");
        }
        String json = llmOutput.substring(start, end + 1);

        // The edit-employee skill returns: { "employeeId": <number>, "updates": { ...fields... } }
        JsonNode root = objectMapper.readTree(json);

        JsonNode idNode = root.get("employeeId");
        if (idNode == null || idNode.isNull()) {
            throw new IllegalArgumentException("LLM output did not contain an employeeId");
        }
        Long employeeId = idNode.asLong();

        JsonNode updatesNode = root.get("updates");
        Employee updates = (updatesNode != null && !updatesNode.isNull())
                ? objectMapper.treeToValue(updatesNode, Employee.class)
                : new Employee();

        log.info("Orchestrator parsed employee ID: {}", employeeId);
        log.info("Orchestrator parsed updates: firstName={}, lastName={}, department={}",
                updates.getFirstName(), updates.getLastName(), updates.getDepartment());

        return employeeService.updateById(employeeId, updates);
    }
}