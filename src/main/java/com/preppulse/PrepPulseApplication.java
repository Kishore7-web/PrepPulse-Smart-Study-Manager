package com.preppulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PrepPulseApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrepPulseApplication.class, args);
    }
}
