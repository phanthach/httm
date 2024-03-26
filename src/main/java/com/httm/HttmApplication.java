package com.httm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HttmApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttmApplication.class, args);
    }

}
