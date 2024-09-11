package com.northeastern.choreless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//(scanBasePackages = {"com.northeastern.controller"})
@SpringBootApplication(scanBasePackages = {"com.northeastern"})
public class ChoreLessApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChoreLessApplication.class, args);
	}

}	
