package com.anb.postgres.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class McpRegistrar {

    private static final Logger log = LoggerFactory.getLogger(McpRegistrar.class);

    @Value("${mcp.registration.url}")
    private String registrationUrl;

    @Value("${mcp.callback.url}")
    private String callbackUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @EventListener(ApplicationReadyEvent.class)
    public void registerSkill(){
        try{
            log.info("Registering MCP skill to {} with callback {}", registrationUrl, callbackUrl);

            Map<String, Object> manifest = new HashMap<>();
            manifest.put("name", "add-employee");
            manifest.put("description", "Add an employee via the application");
            manifest.put("callbackUrl", callbackUrl);
            manifest.put("methods", new String[]{"POST"});

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String,Object>> request = new HttpEntity<>(manifest, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(registrationUrl, request, String.class);
            log.info("MCP registration response: {} {}", response.getStatusCodeValue(), response.getBody());
        }
        catch (Exception ex){
            log.error("Failed to register MCP skill", ex);
        }
    }
}
