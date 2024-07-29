package com.monetize360.quiz.service;

import com.monetize360.quiz.domain.Question;

import java.util.List;

public interface QuizService {
    void startQuiz(String username);
    boolean checkAnswer(int questionIndex, int answerIndex);
    List<Question> getQuestions();
}
