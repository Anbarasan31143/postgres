package com.anb.postgres.services;

import com.anb.postgres.entity.Employee;
import com.anb.postgres.exception.BadRequestException;
import com.anb.postgres.exception.ResourceNotFoundException;
import com.anb.postgres.repository.EmployeeRepository;
import com.anb.postgres.servicesimpl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void testGetEmployeeById_found(){
        Employee emp = new Employee();
        emp.setId(1L);
        emp.setName("Anbu");
        emp.setDepartment("IT");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        Employee result = employeeService.findById(1L);
        assertEquals("Anbu", result.getName());
    }

    @Test
    void testGetEmployeeById_notFound(){
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, ()->employeeService.findById(2L) );
        assertEquals("No employee found given Id: 2" , thrown.getMessage());
    }
    @Test
    void testAddEmployee_success() {

        Employee emp = new Employee();
        emp.setId(null);
        emp.setName("Anbu");
        emp.setDepartment("IT");
        Employee savedEmp = new Employee();
        savedEmp.setId(1L);
        savedEmp.setName("Anbu");
        savedEmp.setDepartment("IT");

        when(employeeRepository.save(emp)).thenReturn(savedEmp);

        Employee result = employeeService.addEmployee(emp);
        assertEquals(1L, result.getId());
    }

    @Test
    void testAddEmployee_duplicateEntry_shouldThrowBadRequest(){

        Employee newEmp  = new Employee();
        newEmp.setId(null);
        newEmp.setName("Anbu");
        newEmp.setDepartment("IT");
        Employee existingEmp = new Employee();
        existingEmp.setId(1L);
        existingEmp.setName("Anbu");
        existingEmp.setDepartment("IT");
        when(employeeRepository.findByNameAndDepartment("Anbu","IT")).thenReturn(Optional.of(existingEmp));
        BadRequestException thrown = assertThrows(BadRequestException.class, ()->employeeService.addEmployee(newEmp));

        assertEquals("Employee already exists with name and department." ,thrown.getMessage());


    }
}
