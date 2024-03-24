package com.example.testspringboot.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculationController {

    @GetMapping
    private String calculate() {
        int result = 10 + 10;
        return "10 + 10" + result;
    }
}
