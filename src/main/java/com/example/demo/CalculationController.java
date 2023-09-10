package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculationController {

    @GetMapping("/calculate")
    private String calculate() {
        int result = 10 + 10;
        return "10 + 10 " + result;
    }
}
