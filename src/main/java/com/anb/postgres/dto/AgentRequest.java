package com.anb.postgres.dto;

import java.util.Map;

public class AgentRequest {
    private String skillName;
    private Map<String,String> variables;

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    public Map<String,String> getVariables() { return variables; }
    public void setVariables(Map<String,String> variables) { this.variables = variables; }
}
