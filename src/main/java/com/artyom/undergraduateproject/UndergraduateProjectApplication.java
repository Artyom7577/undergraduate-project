package com.artyom.undergraduateproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class UndergraduateProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(UndergraduateProjectApplication.class, args);
    }

}
