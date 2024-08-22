package com.monetize360.learning_spring_core.example2;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LearningSpringCoreApplication {

	public static void main(String[] args) {
		ApplicationContext context=SpringApplication.run(LearningSpringCoreApplication.class, args);
		Car car = context.getBean(Car.class);
		car.drive();
		}
	}