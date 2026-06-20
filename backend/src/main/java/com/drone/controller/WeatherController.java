package com.drone.controller;

import com.drone.model.WeatherAssessment;
import com.drone.service.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/assess")
    public WeatherAssessment assess(@RequestBody Map<String, Object> request) {
        double windSpeed = ((Number) request.get("windSpeed")).doubleValue();
        double visibility = ((Number) request.get("visibility")).doubleValue();
        return weatherService.assess(windSpeed, visibility);
    }
}
