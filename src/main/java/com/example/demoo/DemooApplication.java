package com.example.demoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;

@SpringBootApplication
public class DemooApplication {
	public static void main(String[] args) {
		Application.launch(JavaFxApplication.class, args);
	}
}
