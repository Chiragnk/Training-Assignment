package com.monetize360.quiz.service;

import com.monetize360.quiz.domain.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderUtil {

    public static List<Question> loadQuestions(String fileName) {
        List<Question> questions = new ArrayList<>();

        try (InputStream inputStream = CsvReaderUtil.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + fileName);
            }

            String line;
            long id = 1;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    String questionText = br.readLine();
                    List<String> options = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        options.add(br.readLine());
                    }
                    String answerLine = br.readLine();
                    int answer = Integer.parseInt(answerLine.split(":")[1]) - 1;

                    Question question = new Question();
                    question.setId(id++);
                    question.setName(questionText);
                    question.setOptions(options);
                    question.setAnswer(answer);
                    questions.add(question);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questions;
    }
}
