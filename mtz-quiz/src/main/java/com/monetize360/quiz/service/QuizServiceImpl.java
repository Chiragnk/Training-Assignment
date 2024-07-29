package com.monetize360.quiz.service;

import com.monetize360.quiz.domain.Question;


import java.util.List;
import java.util.Scanner;

public class QuizServiceImpl implements QuizService {
    private List<Question> questions;

    @Override
    public void startQuiz(String username) {
        System.out.println("Hello " + username + ", welcome to the Quiz!");
        questions = CsvReaderUtil.loadQuestions("quiz.txt");

        Scanner scanner = new Scanner(System.in);

        int score = 0;

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            System.out.println("Q" + (i + 1) + ": " + question.getName());
            List<String> options = question.getOptions();
            for (int j = 0; j < options.size(); j++) {
                System.out.println((j + 1) + ": " + options.get(j));
            }

            System.out.print("Enter your answer (1-" + options.size() + "): ");
            int userAnswer = scanner.nextInt();

            if (checkAnswer(i, userAnswer - 1)) {
                System.out.println("Correct!\n");
                score++;
            } else {
                System.out.println("Incorrect. The correct answer is " + (question.getAnswer() + 1) + ".\n");
            }
        }

        System.out.println("Quiz finished! Your score: " + score + "/" + questions.size());
        scanner.close();
    }

    @Override
    public boolean checkAnswer(int questionIndex, int answerIndex) {
        Question question = questions.get(questionIndex);
        return question != null && question.getAnswer() == answerIndex;
    }

    @Override
    public List<Question> getQuestions() {
        return questions;
    }

}
