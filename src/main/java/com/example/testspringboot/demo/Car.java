package com.example.testspringboot.demo;

import org.springframework.beans.factory.annotation.Autowired;

public class Car {
    private Engine engine;

    @Autowired
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void drive() {
        engine.start();
        System.out.println("Car is moving!!!");
    }
}