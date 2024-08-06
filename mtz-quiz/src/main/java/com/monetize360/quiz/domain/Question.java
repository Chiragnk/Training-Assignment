package com.monetize360.quiz.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Question {
    private long id;
    private String name;
    private List<String> options;
    private int answer;
}
