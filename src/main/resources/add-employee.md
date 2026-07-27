# add-employee skill

User says: {{userMessage}}

Extract employee information from this message and provide ONLY a valid JSON object.

IMPORTANT RULES:
- phoneNumber MUST be a number (10 digits) or null if not provided
- address is optional, can be null
- firstName and lastName are extracted from the message
- department is extracted from the message
- Do NOT use template placeholders in the output

Example outputs:
If user says "Add employee John Doe from IT department":
{
  "firstName": "John",
  "lastName": "Doe",
  "department": "IT",
  "phoneNumber": null,
  "address": null
}

If user says "Add employee Sarah Smith from HR with phone 8765432109":
{
  "firstName": "Sarah",
  "lastName": "Smith",
  "department": "HR",
  "phoneNumber": 8765432109,
  "address": null
}

If user says "Add employee Mike Johnson from Finance at 456 Oak Street phone 9876543210":
{
  "firstName": "Mike",
  "lastName": "Johnson",
  "department": "Finance",
  "phoneNumber": 9876543210,
  "address": "456 Oak Street"
}

PARSE THE USER MESSAGE AND EXTRACT:
- firstName: First name (required)
- lastName: Last name (required)
- department: Department (required)
- phoneNumber: 10-digit number if found in message, otherwise null
- address: Address if found in message, otherwise null

Return ONLY the JSON object, no other text.
