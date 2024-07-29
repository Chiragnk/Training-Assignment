package com.monetize360;

import com.monetize360.quiz.service.QuizService;
import com.monetize360.quiz.service.QuizServiceImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String username = scanner.nextLine();
        QuizService quizService = new QuizServiceImpl();
        quizService.startQuiz(username);
    }
}