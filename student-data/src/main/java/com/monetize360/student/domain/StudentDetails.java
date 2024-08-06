package com.monetize360.student.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class StudentDetails {
    private String name;
    private String batch;
    private String completed;
    private String placed;
    private String qualification;
    private double score;
}
