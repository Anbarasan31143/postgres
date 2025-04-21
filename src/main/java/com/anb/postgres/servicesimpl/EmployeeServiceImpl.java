package com.anb.postgres.servicesimpl;

import com.anb.postgres.constants.EmployeeConstants;
import com.anb.postgres.constants.ErrorMessages;
import com.anb.postgres.dto.EmployeeResponse;
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


    public EmployeeResponse addEmployee(Employee emp){
        EmployeeResponse employeeResponse = new EmployeeResponse();
       validateEmployee(emp);
       Optional<Employee> duplicate = employeeRepository.findByFisrtNameAndLastNameAndDepartment(emp.getFisrtName(),emp.getLastName(), emp.getDepartment());

        if(duplicate.isPresent()){
            log.info(" Duplicate found " +String.join(" ", emp.getFisrtName(), emp.getLastName()));
            throw new BadRequestException(ErrorMessages.DUPLICATE_EMPLOYEE);
        }
        try{
            // Generate Email
        String email = (emp.getFisrtName() + "." + emp.getLastName() + "@anb.com").toLowerCase();
        emp.setEmailId(email);
        employeeRepository.save(emp);

        employeeResponse.setEmail(email);
        employeeResponse.setMessage(EmployeeConstants.SUCCESS+ emp.getFisrtName() + " " + emp.getLastName());
        return  employeeResponse;
    } catch (Exception e) {
       throw new InternalServerException(ErrorMessages.NETWORK_ISSUE);
    }

    }

    private void validateEmployee(Employee emp){
       if(emp != null){
           if( emp.getFisrtName().isBlank() || emp.getLastName().isBlank()) {
               throw new BadRequestException(ErrorMessages.EMPLOYEE_NAME_REQUIRED);
           }
           if( emp.getDepartment().isBlank() ){
               throw new BadRequestException(ErrorMessages.INVALID_EMPLOYEE_DATA);

           }
           if( emp.getPhoneNumber() != null && !String.valueOf(emp.getPhoneNumber()).matches("\\d{10}")){
               throw new BadRequestException(ErrorMessages.INVALID_CONTACT_NUMNER);
           }

       }
       else {
           throw new BadRequestException(ErrorMessages.INVALID_EMPLOYEE_DATA);
       }

    }
}

