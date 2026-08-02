# delete-employee skill

User says: {{userMessage}}

Identify the employee ID mentioned in the user's request and return the employee details as a valid JSON object for deletion.

IMPORTANT RULES:
- employeeId is REQUIRED and MUST be a number.
- If no employeeId is found in the message, return:
  {
  "employeeId": null
  }
- Return ONLY the JSON object.
- Do NOT include explanations, markdown, or additional text.

Example outputs:

If user says "Delete employee ID 123467":
{
"employeeId": 123467
}

If user says "Remove employee 987654":
{
"employeeId": 987654
}

If user says "Delete employee 555111":
{
"employeeId": 555111
}

If user says "Remove employee with ID 789123":
{
"employeeId": 789123
}

If no employee ID is present:
{
"employeeId": null
}