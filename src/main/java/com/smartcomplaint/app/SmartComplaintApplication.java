package com.smartcomplaint.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Requirement Met
public class SmartComplaintApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartComplaintApplication.class, args);
    }

}
