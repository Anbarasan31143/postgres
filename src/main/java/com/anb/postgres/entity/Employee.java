package com.anb.postgres.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "emp_seq", sequenceName = "emp_sequence", initialValue = 123456, allocationSize = 1)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq")
    private Long employeeId;
    private String fisrtName;
    private String lastName;
    private String department;
    private String emailId;
    private Long phoneNumber;
    private String address;
}
