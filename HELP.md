# Getting Started

### Reference Documentation
This is just for Springboot rest API connect with Postgress database with DML of data
1. Adding Employee data
2. Finding Empployee data
3.Deletion of Employee data

Tech stack
Java 17 , Spring Boot 3.x , Junit 5 -Mock

Step1 : Java 17 download and install
Step2 : Download and install Postgres
Step3 : You can use Spring Initializr (https://start.spring.io/) with the following dependencies:
        Spring Web
        Spring Data JPA
        MySQL Driver (or another DB driver)
        Spring Boot DevTools (optional)

Step 4: Download PostgreSQL Installer
        Go to the official site: https://www.postgresql.org/download/windows/
        Click the "Download the installer" link – it will redirect you to EnterpriseDB.
        Choose the latest version (or one that suits your need) and click Download.
        Run the Installer
        Once downloaded, run the installer .exe file.
        Follow the installation wizard:
        Installation Directory: Leave default or choose your preferred path.
        Select Components: Keep default (PostgreSQL Server, pgAdmin, Stack Builder).
        Data Directory: Leave as is or customize.
        Password: Set a password for the Postgres superuser (postgres) — remember this!
        Port: Default is 5432 — change only if needed.
        Locale: Leave as default unless you need a specific setting.
        Click Next and finish the installation.

   Step 5:     Verify Installation
        After installation, open pgAdmin (GUI for PostgreSQL).
        Login using:
        Username: postgres
        Password: (the one you set during installation)

---

## Step 6: Employee Service API Documentation

### Service Overview
The Employee Service is a REST API built with Spring Boot that manages employee data in a PostgreSQL database. It provides complete CRUD (Create, Read, Update, Delete) operations with comprehensive validation and error handling.

### Key Features:
- **Auto-generated Employee IDs**: 6-digit sequential IDs (starting from 100000)
- **Email Generation**: Auto-generates email in format `firstname.lastname@anb.com`
- **Duplicate Prevention**: Prevents duplicate employees with same name and department
- **Phone Validation**: Validates 10-digit phone numbers
- **Exception Handling**: Comprehensive error handling with meaningful messages
- **Stream API Support**: Efficient batch operations using Java Streams

### Technology Stack:
- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL
- **Testing**: JUnit 5, Mockito
- **ORM**: Spring Data JPA

---

## API Methods

### Base URL:
```
http://localhost:8080/employees
```

### 1. GET ALL EMPLOYEES
**Endpoint**: `GET /employees`

**Description**: Retrieve all employees from the database

**Request**:
```
GET http://localhost:8080/employees
```

**Response** (200 OK):
```json
[
  {
   "employeeId": 100000,
   "fisrtName": "John",
   "lastName": "Doe",
   "department": "IT",
   "emailId": "john.doe@anb.com",
   "phoneNumber": 9876543210,
   "address": "123 Main St"
  },
  {
   "employeeId": 100001,
   "fisrtName": "Jane",
   "lastName": "Smith",
   "department": "HR",
   "emailId": "jane.smith@anb.com",
   "phoneNumber": 9123456789,
   "address": "456 Oak Ave"
  }
]
```

---

### 2. GET EMPLOYEE BY ID
**Endpoint**: `GET /employees/{employeeId}`

**Description**: Retrieve a specific employee by ID

**Request**:
```
GET http://localhost:8080/employees/100000
```

**Response** (200 OK):
```json
{
  "employeeId": 100000,
  "fisrtName": "John",
  "lastName": "Doe",
  "department": "IT",
  "emailId": "john.doe@anb.com",
  "phoneNumber": 9876543210,
  "address": "123 Main St"
}
```

**Response** (404 Not Found):
```json
{
  "error": "No employee found given Id: 100000"
}
```

---

### 3. CREATE EMPLOYEE (ADD)
**Endpoint**: `POST /employees`

**Description**: Create a new employee (email auto-generated)

**Request**:
```
POST http://localhost:8080/employees
Content-Type: application/json

{
  "fisrtName": "Alice",
  "lastName": "Johnson",
  "department": "Engineering",
  "phoneNumber": 9876543210,
  "address": "789 Pine St"
}
```

**Response** (201 Created):
```json
{
  "employeeId": 100002,
  "email": "alice.johnson@anb.com",
  "message": "Successfully added you:Alice Johnson"
}
```

**Response** (400 Bad Request - Duplicate):
```json
{
  "error": "Employee already exists with name and department."
}
```

**Response** (400 Bad Request - Invalid Phone):
```json
{
  "error": "Phone number must be 10 digits"
}
```

---

### 4. UPDATE EMPLOYEE
**Endpoint**: `PUT /employees/{employeeId}`

**Description**: Update an existing employee's details

**Request**:
```
PUT http://localhost:8080/employees/100000
Content-Type: application/json

{
  "fisrtName": "Johnny",
  "lastName": "Updated",
  "department": "Engineering",
  "phoneNumber": 1234567890,
  "address": "999 New St"
}
```

**Response** (200 OK):
```json
{
  "employeeId": 100000,
  "fisrtName": "Johnny",
  "lastName": "Updated",
  "department": "Engineering",
  "emailId": "johnny.updated@anb.com",
  "phoneNumber": 1234567890,
  "address": "999 New St"
}
```

**Response** (404 Not Found):
```json
{
  "error": "No employee found given Id: 100000"
}
```

---

### 5. DELETE EMPLOYEE
**Endpoint**: `DELETE /employees/{employeeId}`

**Description**: Delete an employee by ID

**Request**:
```
DELETE http://localhost:8080/employees/100000
```

**Response** (204 No Content):
```
(Empty body)
```

**Response** (404 Not Found):
```json
{
  "error": "No employee found given Id: 100000"
}
```

---

## Testing with Postman / Bruno

### Setup Instructions:

#### Option 1: Using Postman
1. Download Postman: https://www.postman.com/downloads/
2. Create a new Collection named "Employee API"
3. Add requests as described below

#### Option 2: Using Bruno
1. Download Bruno: https://www.usebruno.com/
2. Create a new Collection named "Employee API"
3. Add requests as described below

---

### Test Cases:

#### Test 1: Add New Employee
```
Name: Add Employee - John Doe
Method: POST
URL: http://localhost:8080/employees
Headers:
  Content-Type: application/json
Body (raw):
{
  "fisrtName": "John",
  "lastName": "Doe",
  "department": "IT",
  "phoneNumber": 9876543210,
  "address": "123 Main St"
}
Expected Response: 201 Created with employeeId: 100000
```

#### Test 2: Add Second Employee
```
Name: Add Employee - Jane Smith
Method: POST
URL: http://localhost:8080/employees
Headers:
  Content-Type: application/json
Body (raw):
{
  "fisrtName": "Jane",
  "lastName": "Smith",
  "department": "HR",
  "phoneNumber": 9123456789,
  "address": "456 Oak Ave"
}
Expected Response: 201 Created with employeeId: 100001
```

#### Test 3: Get All Employees
```
Name: Get All Employees
Method: GET
URL: http://localhost:8080/employees
Expected Response: 200 OK with array of 2 employees
```

#### Test 4: Get Employee by ID
```
Name: Get Employee by ID
Method: GET
URL: http://localhost:8080/employees/100000
Expected Response: 200 OK with John Doe details
```

#### Test 5: Update Employee
```
Name: Update Employee - John Doe
Method: PUT
URL: http://localhost:8080/employees/100000
Headers:
  Content-Type: application/json
Body (raw):
{
  "fisrtName": "Johnny",
  "lastName": "Updated",
  "department": "Engineering",
  "phoneNumber": 1234567890,
  "address": "999 New St"
}
Expected Response: 200 OK with updated details
```

#### Test 6: Duplicate Employee (Should Fail)
```
Name: Add Duplicate Employee (Should Fail)
Method: POST
URL: http://localhost:8080/employees
Headers:
  Content-Type: application/json
Body (raw):
{
  "fisrtName": "Jane",
  "lastName": "Smith",
  "department": "HR",
  "phoneNumber": 9123456789,
  "address": "999 Duplicate St"
}
Expected Response: 400 Bad Request - "Employee already exists with name and department."
```

#### Test 7: Invalid Phone Number (Should Fail)
```
Name: Add Employee - Invalid Phone (Should Fail)
Method: POST
URL: http://localhost:8080/employees
Headers:
  Content-Type: application/json
Body (raw):
{
  "fisrtName": "Bob",
  "lastName": "Invalid",
  "department": "Sales",
  "phoneNumber": 123,
  "address": "Invalid Address"
}
Expected Response: 400 Bad Request - "Phone number must be 10 digits"
```

#### Test 8: Delete Employee
```
Name: Delete Employee
Method: DELETE
URL: http://localhost:8080/employees/100001
Expected Response: 204 No Content
```

#### Test 9: Get Deleted Employee (Should Fail)
```
Name: Get Deleted Employee (Should Fail)
Method: GET
URL: http://localhost:8080/employees/100001
Expected Response: 404 Not Found - "No employee found given Id: 100001"
```

---

## Running Tests

### Unit Tests:
```bash
# Run all tests
mvn test

# Run only Employee Service tests
mvn test -Dtest=EmployeeServiceTest

# Run specific test
mvn test -Dtest=EmployeeServiceTest#testAddEmployee_success
```

### Building the Project:
```bash
# Clean and build
mvn clean install

# Run the application
mvn spring-boot:run
```

---

## Validation Rules

| Field | Rule | Example |
|-------|------|---------|
| firstName | Required, cannot be blank | "John" |
| lastName | Required, cannot be blank | "Doe" |
| department | Required, cannot be blank | "IT" |
| phoneNumber | Optional, must be 10 digits if provided | 9876543210 |
| address | Optional | "123 Main St" |
| emailId | Auto-generated, lowercase format | "john.doe@anb.com" |

---

## Error Codes

| Code | Message | Cause |
|------|---------|-------|
| 400 | First name and last name are required | Empty firstName or lastName |
| 400 | Invalid employee data | Empty department or null employee |
| 400 | Phone number must be 10 digits | Invalid phone format |
| 400 | Employee already exists with name and department | Duplicate employee |
| 404 | No employee found given Id: {id} | Employee ID not found |
| 500 | Network error | Database or server issue |
| 500 | Deletion failed | Error during delete operation |

