package com.example.testspringboot.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CalculationController {

    @GetMapping
    private String calculate() {
        int result = 10 + 10;
        return "10 + 10" + result;
    }

    @GetMapping("/qrTest")
    public Map<String, Object> qrTest() {
        Map<String, Object> applinks = new HashMap<>();
        applinks.put("apps", new String[]{});

        Map<String, Object> detailsItem = new HashMap<>();
        detailsItem.put("appID", "Z83T53XK53.com.sketch-wallet.ios");
        detailsItem.put("paths", new String[]{"/SketchWallet/*"});

        applinks.put("details", new Map[]{detailsItem});

        Map<String, Object> response = new HashMap<>();
        response.put("applinks", applinks);

        return response;
    }
}
