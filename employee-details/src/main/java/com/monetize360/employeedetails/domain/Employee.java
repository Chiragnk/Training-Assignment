package com.monetize360.employeedetails.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee {
    private int empno;
    private double salary;
    private String job;
    private String currency;
    private String salaryString;
}
