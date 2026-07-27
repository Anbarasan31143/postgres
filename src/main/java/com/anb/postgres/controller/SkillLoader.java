package com.anb.postgres.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class SkillLoader {

    public String loadSkill(String skillName) throws Exception{
        // try skills/ first, then root
        var resource = new ClassPathResource("skills/" + skillName + ".md");
        if(!resource.exists()){
            resource = new ClassPathResource(skillName + ".md");
        }
        try(InputStream is = resource.getInputStream()){
            return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
    }
}
