package com.anb.postgres.controller;

import com.anb.postgres.dto.ChatRequest;
import com.anb.postgres.dto.ChatResponse;
import com.anb.postgres.dto.EmployeeResponse;
import com.anb.postgres.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private AgentOrchestrator agentOrchestrator;

    @PostMapping("/message")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        try {
            log.info("Received chat message: {}", request.getMessage());
            
            String userMessage = request.getMessage().trim();
            
            // Check if user wants to add employee
            if (userMessage.toLowerCase().contains("add employee")) {
                // Create a map with the user message for the orchestrator
                Map<String, String> variables = new java.util.HashMap<>();
                variables.put("userMessage", userMessage);
                
                EmployeeResponse employeeResp = agentOrchestrator.orchestrateAddEmployee("add-employee", variables);
                
                ChatResponse response = new ChatResponse(
                    "success",
                    employeeResp.getMessage(),
                    employeeResp
                );
                return ResponseEntity.ok(response);
            }
            else if(userMessage.toLowerCase().contains("update employee") || userMessage.toLowerCase().contains("edit employee")){
                // Create a map with the user message for the orchestrator
                Map<String, String> variables = new java.util.HashMap<>();
                variables.put("userMessage", userMessage);

                Employee employeeResp = agentOrchestrator.orchestrateUpdateEmployee("edit-employee", variables);

                ChatResponse response = new ChatResponse(
                        "success",
                        "Employee updated successfully",
                        employeeResp
                );
                return ResponseEntity.ok(response);


            }
            
            // Default response for other messages
            ChatResponse response = new ChatResponse(
                "info",
                "I can help you add employees. Try saying: 'Add employee John Doe from IT department with email john@example.com'"
            );
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error processing chat message", e);
            ChatResponse errorResponse = new ChatResponse(
                "error",
                "Failed to process request: " + e.getMessage()
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<ChatResponse> health() {
        return ResponseEntity.ok(new ChatResponse("success", "Chat service is running"));
    }
}
