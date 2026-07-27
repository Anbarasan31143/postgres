package com.anb.postgres.util;

import java.util.Set;

public enum EmployeeIntent {

    ADD(Set.of(
            "add employee",
            "create employee",
            "new employee",
            "register employee"
    )),
    FIND(Set.of(
            "find employee",
            "get employee",
            "show employee",
            "retrieve employee",
            "lookup employee"
    )),

    EDIT(Set.of(
            "edit employee",
            "update employee",
            "modify employee"
    )),
    DELETE(Set.of(
            "delete employee",
            "remove employee"
    ));

    private final Set<String> phrases;

    EmployeeIntent(Set<String> phrases) {
        this.phrases = phrases;
    }

    public boolean matches(String message) {
        String lowerMessage = message.toLowerCase();
        return phrases.stream().anyMatch(lowerMessage::contains);
    }
}
