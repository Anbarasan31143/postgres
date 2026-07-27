package com.anb.postgres.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mcp")
public class McpRegistrationController {

    private static final Logger log = LoggerFactory.getLogger(McpRegistrationController.class);

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerSkill(@RequestBody Map<String, Object> manifest) {
        log.info("Received MCP skill registration: {}", manifest.get("name"));
        
        Map<String, Object> response = Map.of(
            "status", "success",
            "message", "MCP skill registered successfully",
            "skillName", manifest.get("name")
        );
        
        return ResponseEntity.ok(response);
    }
}
