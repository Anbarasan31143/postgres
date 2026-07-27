# edit-employee skill

User says: {{userMessage}}

Extract employee update information from this message and provide ONLY a valid JSON object.

IMPORTANT RULES:
- employeeId is REQUIRED — it identifies which employee to update. It MUST be a number.
- If no employeeId is found in the message, set employeeId to null
- Only include fields in "updates" that the user actually wants changed — do NOT include fields that were not mentioned
- phoneNumber MUST be a number (10 digits) if provided
- Do NOT use template placeholders in the output

Example outputs:

If user says "Update employee ID 4521's department to Finance":
{
"employeeId": 4521,
"updates": {
"department": "Finance"
}
}

If user says "For employee 8832, change phone number to 9123456789":
{
"employeeId": 8832,
"updates": {
"phoneNumber": 9123456789
}
}

If user says "Edit employee ID 4521, set address to 12 Elm Street and department to HR":
{
"employeeId": 4521,
"updates": {
"address": "12 Elm Street",
"department": "HR"
}
}

If user says "Update employee 1023: last name to Johnston, phone 8887776665":
{
"employeeId": 1023,
"updates": {
"lastName": "Johnston",
"phoneNumber": 8887776665
}
}

If user says "Update John Doe's department to Finance" (no employeeId given):
{
"employeeId": null,
"updates": {
"department": "Finance"
}
}

PARSE THE USER MESSAGE AND EXTRACT:
- employeeId: the employee ID mentioned in the message (number), otherwise null
- updates: object containing ONLY the fields the user wants changed, using these possible keys:
    - firstName
    - lastName
    - department
    - phoneNumber (10-digit number)
    - address

Return ONLY the JSON object, no other text.
