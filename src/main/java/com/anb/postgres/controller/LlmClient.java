package com.anb.postgres.controller;

public interface LlmClient {
    String generate(String prompt) throws Exception;
}
