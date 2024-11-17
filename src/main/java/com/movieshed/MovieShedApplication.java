package com.movieshed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EntityScan
@Configuration
public class MovieShedApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieShedApplication.class, args);
        }
}

