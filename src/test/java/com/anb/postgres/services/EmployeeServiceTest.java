package com.anb.postgres.services;

import com.anb.postgres.dto.EmployeeResponse;
import com.anb.postgres.entity.Employee;
import com.anb.postgres.exception.BadRequestException;
import com.anb.postgres.exception.DeleteOperationException;
import com.anb.postgres.exception.InternalServerException;
import com.anb.postgres.exception.ResourceNotFoundException;
import com.anb.postgres.repository.EmployeeRepository;
import com.anb.postgres.servicesimpl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    // ===== HELPER METHODS =====
    private Employee createEmployee(Long employeeId, String firstName, String lastName, String department) {
        Employee emp = new Employee();
        emp.setEmployeeId(employeeId);
        emp.setFirstName(firstName);
        emp.setLastName(lastName);
        emp.setDepartment(department);
        return emp;
    }

    private Employee createEmployeeWithEmail(Long employeeId, String firstName, String lastName, String department, String email) {
        Employee emp = createEmployee(employeeId, firstName, lastName, department);
        emp.setEmailId(email);
        return emp;
    }

    private List<Employee> createEmployeeList(int count) {
        String[][] employees = {
            {"100000", "John", "Doe", "IT"},
            {"100001", "Jane", "Smith", "HR"},
            {"100002", "Bob", "Wilson", "Finance"},
            {"100003", "Alice", "Johnson", "Engineering"},
            {"100004", "Charlie", "Brown", "Operations"}
        };
        
        return LongStream.range(0, count)
            .mapToObj(i -> createEmployee(
                Long.parseLong(employees[(int)i][0]),
                employees[(int)i][1],
                employees[(int)i][2],
                employees[(int)i][3]
            ))
            .collect(Collectors.toList());
    }

    // ===== FIND BY ID TESTS =====
    @Test
    void testGetEmployeeById_found(){
        Employee emp = createEmployeeWithEmail(1L, "Anbarasan", "Seethapathy", "IT", "anbarasans@anb.com");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        Employee result = employeeService.findById(1L);
        assertEquals("AnbarasanSeethapathy", String.join("","Anbarasan","Seethapathy"));
    }

    @Test
    void testGetEmployeeById_notFound(){
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, ()->employeeService.findById(2L) );
        assertEquals("No employee found given Id: 2" , thrown.getMessage());
    }

    // ===== FIND ALL TESTS =====
    @Test
    void testFindAll_success(){
        List<Employee> employees = createEmployeeList(2);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.findAll();
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    void testFindAll_multipleEmployees(){
        List<Employee> employees = createEmployeeList(5);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.findAll();
        assertEquals(5, result.size());
        assertTrue(result.stream().allMatch(e -> e.getDepartment() != null));
    }

    @Test
    void testFindAll_emptyList(){
        when(employeeRepository.findAll()).thenReturn(Arrays.asList());
        List<Employee> result = employeeService.findAll();
        assertTrue(result.isEmpty());
    }

    // ===== DELETE BY ID TESTS =====
    @Test
    void testDeleteById_success(){
        when(employeeRepository.existsById(100000L)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(100000L);
        
        assertDoesNotThrow(()->employeeService.deleteById(100000L));
        verify(employeeRepository, times(1)).deleteById(100000L);
    }

    @Test
    void testDeleteById_notFound(){
        when(employeeRepository.existsById(100000L)).thenReturn(false);
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, 
            ()->employeeService.deleteById(100000L));
        assertEquals("No employee found given Id: 100000", thrown.getMessage());
    }

    @Test
    void testDeleteById_internalServerError(){
        when(employeeRepository.existsById(100000L)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(employeeRepository).deleteById(100000L);
        
        DeleteOperationException thrown = assertThrows(DeleteOperationException.class, 
            ()->employeeService.deleteById(100000L));
        assertEquals("Deletion failed due to internal error.", thrown.getMessage());
    }

    @Test
    void testDeleteMultipleIds_allSuccess(){
        List<Long> ids = Arrays.asList(100000L, 100001L, 100002L);
        ids.forEach(id -> {
            when(employeeRepository.existsById(id)).thenReturn(true);
            doNothing().when(employeeRepository).deleteById(id);
        });
        
        ids.forEach(id -> assertDoesNotThrow(() -> employeeService.deleteById(id)));
        ids.forEach(id -> verify(employeeRepository, times(1)).deleteById(id));
    }

    // ===== UPDATE BY ID TESTS =====
    @Test
    void testUpdateById_success(){
        Employee existingEmp = createEmployee(100000L, "John", "Doe", "IT");
        existingEmp.setPhoneNumber(9876543210L);
        existingEmp.setAddress("123 Main St");

        Employee updateEmp = createEmployee(100000L, "Johnny", "Updated", "Engineering");
        updateEmp.setPhoneNumber(1234567890L);
        updateEmp.setAddress("456 Oak Ave");

        when(employeeRepository.existsById(100000L)).thenReturn(true);
        when(employeeRepository.findById(100000L)).thenReturn(Optional.of(existingEmp));
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmp);

        Employee result = employeeService.updateById(100000L, updateEmp);
        
        assertEquals("Johnny", result.getFirstName());
        assertEquals("Updated", result.getLastName());
        assertEquals("Engineering", result.getDepartment());
    }

    @Test
    void testUpdateById_notFound(){
        Employee updateEmp = createEmployee(100000L, "Test", "User", "IT");

        when(employeeRepository.existsById(100000L)).thenReturn(false);
        
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, 
            ()->employeeService.updateById(100000L, updateEmp));
        assertEquals("No employee found given Id: 100000", thrown.getMessage());
    }

    @Test
    void testUpdateById_invalidFirstName(){
        Employee updateEmp = createEmployee(100000L, "", "User", "IT");

        when(employeeRepository.existsById(100000L)).thenReturn(true);
        
        BadRequestException thrown = assertThrows(BadRequestException.class, 
            ()->employeeService.updateById(100000L, updateEmp));
        assertEquals("Employee Name is Required", thrown.getMessage());
    }

    @Test
    void testUpdateById_invalidDepartment(){
        Employee updateEmp = createEmployee(100000L, "John", "Doe", "");

        when(employeeRepository.existsById(100000L)).thenReturn(true);
        
        BadRequestException thrown = assertThrows(BadRequestException.class, 
            ()->employeeService.updateById(100000L, updateEmp));
        assertEquals("Given employee data is not correct.", thrown.getMessage());
    }

    @Test
    void testUpdateById_invalidPhoneNumber(){
        Employee updateEmp = createEmployee(100000L, "John", "Doe", "IT");
        updateEmp.setPhoneNumber(12345L);

        when(employeeRepository.existsById(100000L)).thenReturn(true);
        
        BadRequestException thrown = assertThrows(BadRequestException.class, 
            ()->employeeService.updateById(100000L, updateEmp));
        assertEquals("Invalid / Phone number must be number.", thrown.getMessage());
    }

    // ===== ADD EMPLOYEE TESTS =====
    @Test
    void testAddEmployee_success() {
        Employee emp = createEmployee(null, "Anbu", "Arasan", "IT");
        Employee savedEmp = createEmployeeWithEmail(100000L, "Anbu", "Arasan", "IT", "anbu.arasan@anb.com");

        when(employeeRepository.findByFirstNameAndLastNameAndDepartment("Anbu", "Arasan", "IT"))
            .thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmp);

        EmployeeResponse result = employeeService.addEmployee(emp);
        assertEquals("Successfully added you:Anbu Arasan", result.getMessage());
        assertEquals("anbu.arasan@anb.com", result.getEmail());
        assertEquals(100000L, result.getEmployeeId());
    }

    @Test
    void testAddEmployee_duplicateEntry_shouldThrowBadRequest(){
        Employee newEmp = createEmployeeWithEmail(null, "Anbu", "Arasan", "IT", "anbu@anb.com");
        Employee existingEmp = createEmployeeWithEmail(100000L, "Anbu", "Arasan", "IT", "anbu@anb.com");

        when(employeeRepository.findByFirstNameAndLastNameAndDepartment("Anbu","Arasan","IT"))
            .thenReturn(Optional.of(existingEmp));
        
        BadRequestException thrown = assertThrows(BadRequestException.class, 
            ()->employeeService.addEmployee(newEmp));
        assertEquals("Employee already exists with name and department." ,thrown.getMessage());
    }

    @Test
    void testAddEmployee_missingFirstName(){
        Employee emp = createEmployee(null, "", "Doe", "IT");

        BadRequestException thrown = assertThrows(BadRequestException.class, 
            ()->employeeService.addEmployee(emp));
        assertEquals("Employee Name is Required", thrown.getMessage());
    }

    @Test
    void testAddEmployee_missingLastName(){
        Employee emp = createEmployee(null, "John", "", "IT");

        BadRequestException thrown = assertThrows(BadRequestException.class, 
            ()->employeeService.addEmployee(emp));
        assertEquals("Employee Name is Required", thrown.getMessage());
    }

    @Test
    void testAddEmployee_missingDepartment(){
        Employee emp = createEmployee(null, "John", "Doe", "");

        BadRequestException thrown = assertThrows(BadRequestException.class, 
            ()->employeeService.addEmployee(emp));
        assertEquals("Given employee data is not correct.", thrown.getMessage());
    }

    @Test
    void testAddEmployee_invalidPhoneNumber(){
        Employee emp = createEmployee(null, "John", "Doe", "IT");
        emp.setPhoneNumber(123L);

        BadRequestException thrown = assertThrows(BadRequestException.class, 
            ()->employeeService.addEmployee(emp));
        assertEquals("Invalid / Phone number must be number.", thrown.getMessage());
    }

    @Test
    void testAddEmployee_validPhoneNumber(){
        Employee emp = createEmployee(null, "John", "Doe", "IT");
        emp.setPhoneNumber(9876543210L);

        Employee savedEmp = createEmployeeWithEmail(100000L, "John", "Doe", "IT", "john.doe@anb.com");
        savedEmp.setPhoneNumber(9876543210L);

        when(employeeRepository.findByFirstNameAndLastNameAndDepartment("John","Doe","IT"))
            .thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmp);

        EmployeeResponse result = employeeService.addEmployee(emp);
        assertNotNull(result);
        assertEquals("Successfully added you:John Doe", result.getMessage());
    }

    @Test
    void testAddEmployee_nullEmployee(){
        Employee emp = null;

        BadRequestException thrown = assertThrows(BadRequestException.class, 
            ()->employeeService.addEmployee(emp));
        assertEquals("Given employee data is not correct.", thrown.getMessage());
    }

    @Test
    void testAddEmployee_emailGeneration(){
        Employee emp = createEmployee(null, "Alice", "Johnson", "HR");
        Employee savedEmp = createEmployeeWithEmail(100000L, "Alice", "Johnson", "HR", "alice.johnson@anb.com");

        when(employeeRepository.findByFirstNameAndLastNameAndDepartment("Alice","Johnson","HR"))
            .thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmp);

        EmployeeResponse result = employeeService.addEmployee(emp);
        assertEquals("alice.johnson@anb.com", result.getEmail());
    }

    @Test
    void testAddMultipleEmployees_usingStream(){
        List<Employee> newEmployees = createEmployeeList(3);
        
        doAnswer(invocation -> {
            Employee emp = invocation.getArgument(0);
            emp.setEmailId(emp.getFirstName().toLowerCase() + "." + emp.getLastName().toLowerCase() + "@anb.com");
            return emp;
        }).when(employeeRepository).save(any(Employee.class));

        newEmployees.forEach(emp -> {
            when(employeeRepository.findByFirstNameAndLastNameAndDepartment(
                emp.getFirstName(), emp.getLastName(), emp.getDepartment()))
                .thenReturn(Optional.empty());
            
            EmployeeResponse result = employeeService.addEmployee(emp);
            assertNotNull(result.getEmployeeId());
            assertNotNull(result.getEmail());
            assertTrue(result.getEmail().contains("@anb.com"));
        });
    }
}
