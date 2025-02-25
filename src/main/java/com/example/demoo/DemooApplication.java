package com.example.demoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class DemooApplication {
	public static void main(String[] args) {
		Application.launch(JavaFxApplication.class, args);
	}
}
