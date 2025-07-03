package com.miniBankingApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class MiniBankingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniBankingAppApplication.class, args);
	}

}
