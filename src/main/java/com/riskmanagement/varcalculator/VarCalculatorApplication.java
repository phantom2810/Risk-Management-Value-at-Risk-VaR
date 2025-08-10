package com.riskmanagement.varcalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VarCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VarCalculatorApplication.class, args);
    }
}
