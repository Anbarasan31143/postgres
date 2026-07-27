package com.anb.postgres.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MockLlmClient implements LlmClient {
    private static final Logger log = LoggerFactory.getLogger(MockLlmClient.class);

    @Override
    public String generate(String prompt){
        log.info("Mock LLM received prompt");
        
        // Extract user message from prompt
        String userMessage = extractUserMessage(prompt);
        log.info("Mock LLM processing user message: {}", userMessage);
        
        // Parse employee data from user message
        String firstName = extractFirstName(userMessage);
        String lastName = extractLastName(userMessage);
        String department = extractDepartment(userMessage);
        Long phoneNumber = extractPhoneNumber(userMessage);
        String address = extractAddress(userMessage);
        
        // Generate JSON response
        String json = String.format(
            "{\n" +
            "  \"firstName\": \"%s\",\n" +
            "  \"lastName\": \"%s\",\n" +
            "  \"department\": \"%s\",\n" +
            "  \"phoneNumber\": %s,\n" +
            "  \"address\": %s\n" +
            "}",
            firstName,
            lastName,
            department,
            phoneNumber != null ? phoneNumber : "null",
            address != null ? "\"" + address + "\"" : "null"
        );
        
        log.info("Mock LLM generated JSON: {}", json);
        return json;
    }
    
    private String extractUserMessage(String prompt) {
        // Extract the user message after "User says: "
        Pattern p = Pattern.compile("User says: (.+?)(?:\\n|$)", Pattern.DOTALL);
        Matcher m = p.matcher(prompt);
        if (m.find()) {
            return m.group(1).trim();
        }
        return prompt;
    }
    
    private String extractFirstName(String message) {
        // Look for "Add employee FirstName LastName"
        Pattern p = Pattern.compile("(?i)add\\s+employee\\s+(\\w+)\\s+(\\w+)");
        Matcher m = p.matcher(message);
        if (m.find()) {
            return m.group(1);
        }
        return "Unknown";
    }
    
    private String extractLastName(String message) {
        // Look for "Add employee FirstName LastName"
        Pattern p = Pattern.compile("(?i)add\\s+employee\\s+(\\w+)\\s+(\\w+)");
        Matcher m = p.matcher(message);
        if (m.find()) {
            return m.group(2);
        }
        return "User";
    }
    
    private String extractDepartment(String message) {
        // Look for "from Department" or "from IT department"
        Pattern p = Pattern.compile("(?i)from\\s+(\\w+)(?:\\s+department)?");
        Matcher m = p.matcher(message);
        if (m.find()) {
            return m.group(1);
        }
        return "IT";
    }
    
    private Long extractPhoneNumber(String message) {
        // Look for 10-digit phone number
        Pattern p = Pattern.compile("(?:\\D|^)(\\d{10})(?:\\D|$)");
        Matcher m = p.matcher(message);
        if (m.find()) {
            try {
                return Long.parseLong(m.group(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    private String extractAddress(String message) {
        // Look for "at address" or "address is"
        Pattern p = Pattern.compile("(?i)at\\s+(.+?)(?:\\s+phone|\\s+department|$)");
        Matcher m = p.matcher(message);
        if (m.find()) {
            return m.group(1).trim();
        }
        return null;
    }
}

