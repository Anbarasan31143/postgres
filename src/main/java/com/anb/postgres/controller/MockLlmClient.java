package com.anb.postgres.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
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

        String json;
        if (isEditSkill(prompt)) {
            json = generateEditJson(userMessage);
        } else {
            json = generateAddJson(userMessage);
        }

        log.info("Mock LLM generated JSON: {}", json);
        return json;
    }

    /**
     * Decide which skill's prompt template we were handed. The edit-employee
     * template always mentions "employeeId" as the required identifier, while
     * the add-employee template does not, so we key off that.
     */
    private boolean isEditSkill(String prompt) {
        return prompt != null && prompt.toLowerCase().contains("employeeid");
    }

    // ---------- add-employee ----------

    private String generateAddJson(String message) {
        String firstName = extractFirstName(message);
        String lastName = extractLastName(message);
        String department = extractDepartment(message);
        Long phoneNumber = extractPhoneNumber(message);
        String address = extractAddress(message);

        return String.format(
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
    }

    private String extractFirstName(String message) {
        Pattern p = Pattern.compile("(?i)add\\s+employee\\s+(\\w+)\\s+(\\w+)");
        Matcher m = p.matcher(message);
        if (m.find()) {
            return m.group(1);
        }
        return "Unknown";
    }

    private String extractLastName(String message) {
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

    private String extractAddress(String message) {
        Pattern p = Pattern.compile("(?i)at\\s+(.+?)(?:\\s+phone|\\s+department|$)");
        Matcher m = p.matcher(message);
        if (m.find()) {
            return m.group(1).trim();
        }
        return null;
    }

    // ---------- edit-employee ----------

    private String generateEditJson(String message) {
        Long employeeId = extractEmployeeId(message);

        Map<String, String> updates = new LinkedHashMap<>();

        String department = extractUpdateDepartment(message);
        if (department != null) {
            updates.put("department", "\"" + department + "\"");
        }

        String firstName = extractUpdateField(message, "first name");
        if (firstName != null) {
            updates.put("firstName", "\"" + firstName + "\"");
        }

        String lastName = extractUpdateField(message, "last name");
        if (lastName != null) {
            updates.put("lastName", "\"" + lastName + "\"");
        }

        String address = extractUpdateAddress(message);
        if (address != null) {
            updates.put("address", "\"" + address + "\"");
        }

        Long phoneNumber = extractUpdatePhoneNumber(message);
        if (phoneNumber != null) {
            updates.put("phoneNumber", String.valueOf(phoneNumber));
        }

        StringBuilder updatesJson = new StringBuilder("{\n");
        int i = 0;
        for (Map.Entry<String, String> e : updates.entrySet()) {
            updatesJson.append("    \"").append(e.getKey()).append("\": ").append(e.getValue());
            if (++i < updates.size()) {
                updatesJson.append(",");
            }
            updatesJson.append("\n");
        }
        updatesJson.append("  }");

        return String.format(
                "{\n" +
                        "  \"employeeId\": %s,\n" +
                        "  \"updates\": %s\n" +
                        "}",
                employeeId != null ? employeeId : "null",
                updatesJson
        );
    }

    private Long extractEmployeeId(String message) {
        // Matches "employee ID 123466", "employee 123466", "employee id 123466"
        Pattern p = Pattern.compile("(?i)employee\\s*(?:id)?\\s*[:#]?\\s*(\\d+)");
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

    private String extractUpdateDepartment(String message) {
        // Matches "department to Finance", "department: Finance", "department Finance"
        Pattern p = Pattern.compile("(?i)department\\s+(?:to\\s+|is\\s+|[:=]\\s*)?([A-Za-z]+)");
        Matcher m = p.matcher(message);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private String extractUpdateField(String message, String fieldLabel) {
        // Matches "<fieldLabel> to Value", "<fieldLabel>: Value", "<fieldLabel> Value"
        Pattern p = Pattern.compile("(?i)" + Pattern.quote(fieldLabel) + "\\s+(?:to\\s+|is\\s+|[:=]\\s*)?([A-Za-z]+)");
        Matcher m = p.matcher(message);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private String extractUpdateAddress(String message) {
        // Matches "address to 12 Elm Street", "address: 12 Elm Street"
        Pattern p = Pattern.compile("(?i)address\\s+(?:to\\s+|is\\s+|[:=]\\s*)?(.+?)(?:\\s+and\\b|,|$)");
        Matcher m = p.matcher(message);
        if (m.find()) {
            return m.group(1).trim();
        }
        return null;
    }

    private Long extractUpdatePhoneNumber(String message) {
        // Only treat a 10-digit number as a phone update if "phone" appears nearby
        if (!message.toLowerCase().contains("phone")) {
            return null;
        }
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

    // ---------- shared ----------

    private String extractUserMessage(String prompt) {
        Pattern p = Pattern.compile("User says: (.+?)(?:\\n|$)", Pattern.DOTALL);
        Matcher m = p.matcher(prompt);
        if (m.find()) {
            return m.group(1).trim();
        }
        return prompt;
    }

    private Long extractPhoneNumber(String message) {
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
}