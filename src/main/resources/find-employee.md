# find-employee skill

User says: {{userMessage}}

Identify the employee ID mentioned in the user's request and return the employee details as a valid JSON object.

IMPORTANT RULES:
- employeeId is REQUIRED and MUST be a number.
- If no employeeId is found in the message, return:
  {
  "employeeId": null
  }
- Return ONLY the JSON object.
- Do NOT include explanations, markdown, or additional text.

Example outputs:

If user says "Find employee ID 123467":
{
"employeeId": 123467,
"firstName": "Mike",
"lastName": "Johnson",
"department": "Finance",
"emailId": "mike.johnson@anb.com",
"phoneNumber": 9876543210,
"address": "456 Oak Street"
}

If user says "Show details of employee 987654":
{
"employeeId": 987654,
"firstName": "Sarah",
"lastName": "Williams",
"department": "Human Resources",
"emailId": "sarah.williams@anb.com",
"phoneNumber": 9123456789,
"address": "789 Pine Avenue"
}

If user says "Get employee 555111":
{
"employeeId": 555111,
"firstName": "David",
"lastName": "Brown",
"department": "Engineering",
"emailId": "david.brown@anb.com",
"phoneNumber": 9988776655,
"address": "12 Park Road"
}

If no employee ID is present:
{
"employeeId": null
}