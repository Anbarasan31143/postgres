package com.anb.postgres.servicesimpl;

import com.anb.postgres.constants.ErrorMessages;
import com.anb.postgres.entity.Employee;
import com.anb.postgres.exception.BadRequestException;
import com.anb.postgres.exception.DeleteOperationException;
import com.anb.postgres.exception.InternalServerException;
import com.anb.postgres.exception.ResourceNotFoundException;
import com.anb.postgres.repository.EmployeeRepository;
import com.anb.postgres.services.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

   @Autowired
    private EmployeeRepository employeeRepository;

   @Override
    public Employee findById(Long Id) {
      return  employeeRepository.findById(Id)
              .orElseThrow(()-> new ResourceNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND + Id));
   }

    @Override
    public void deleteById(Long Id) {
       if(!employeeRepository.existsById(Id)){
           throw new ResourceNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND +Id);
       }
       try {
           employeeRepository.deleteById(Id);
       }
       catch (Exception ex){
           throw  new DeleteOperationException(ErrorMessages.DELETION_FAILED);
       }
    }


    public Employee addEmployee(Employee emp){
        Optional<Employee> duplicate = employeeRepository.findByNameAndDepartment(emp.getName(),emp.getDepartment());

        if(emp == null || emp.getName().isBlank()) {
            throw new BadRequestException(ErrorMessages.EMPLOYEE_NAME_REQUIRED);
        }
        if(emp == null || emp.getDepartment().isBlank()){
            throw new BadRequestException(ErrorMessages.INVALID_EMPLOYEE_DATA);

        }
        if(duplicate.isPresent()){
            log.info(" Duplicate found " +emp.getName() + " :" +emp.getDepartment());
            throw new BadRequestException(ErrorMessages.DUPLICATE_EMPLOYEE);
        }
        try{
        return employeeRepository.save(emp);
    } catch (Exception e) {
       throw new InternalServerException(ErrorMessages.NETWORK_ISSUE);
    }

    }

}

