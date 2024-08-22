package com.monetize360.learning_spring_core.example1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

    @SpringBootApplication
    public class LearningSpringCoreApplication {

        public static void main(String[] args) {
            ApplicationContext context=SpringApplication.run(com.monetize360.learning_spring_core.example1.LearningSpringCoreApplication.class, args);
            MessagePrinter printer = context.getBean(MessagePrinter.class);
            printer.printMessage();
        }
    }

