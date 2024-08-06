package com.monetize360.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class StudentDTO {
    private String name;
    private String qualification;
    private double score;
}
