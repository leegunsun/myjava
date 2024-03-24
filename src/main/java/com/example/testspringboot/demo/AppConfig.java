package com.example.testspringboot.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public  Engine engine() {
        return new Engine();
    }

    @Bean
    public  Car car() {
        return new Car();
    }
}
