package com.anb.postgres.services;

import com.anb.postgres.dto.EmployeeResponse;
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
        emp.setFisrtName("Anbarasan");
        emp.setLastName("Seethapathy");
        emp.setEmailId("anbarasans@anb.com");
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
    @Test
    void testAddEmployee_success() {

        Employee emp = new Employee();
        emp.setId(null);
        emp.setFisrtName("Anbu");
        emp.setLastName("Arasan");
        emp.setDepartment("IT");
        Employee savedEmp = new Employee();
        savedEmp.setId(1L);
        savedEmp.setFisrtName("Anbu");
        savedEmp.setLastName("Arasan");
        savedEmp.setDepartment("IT");

        when(employeeRepository.save(emp)).thenReturn(savedEmp);

        EmployeeResponse result = employeeService.addEmployee(emp);
        assertEquals("Successfully added you:Anbu Arasan", result.getMessage());
    }

    @Test
    void testAddEmployee_duplicateEntry_shouldThrowBadRequest(){

        Employee newEmp  = new Employee();
        newEmp.setId(null);
        newEmp.setFisrtName("Anbu");
        newEmp.setLastName("Arasan");
        newEmp.setEmailId("anbu@anb.com");
        newEmp.setDepartment("IT");
        Employee existingEmp = new Employee();
        existingEmp.setId(1L);
        existingEmp.setFisrtName("Anbu");
        existingEmp.setLastName("Arasan");
        existingEmp.setEmailId("anbu@anb.com");
        existingEmp.setDepartment("IT");
        when(employeeRepository.findByFisrtNameAndLastNameAndDepartment("Anbu","Arasan","IT")).thenReturn(Optional.of(existingEmp));
        BadRequestException thrown = assertThrows(BadRequestException.class, ()->employeeService.addEmployee(newEmp));

        assertEquals("Employee already exists with name and department." ,thrown.getMessage());


    }
}
