package com.make.plan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CalenderCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalenderCrudApplication.class, args);
    }

}
