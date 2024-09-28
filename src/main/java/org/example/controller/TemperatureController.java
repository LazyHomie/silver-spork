package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.model.City;
import org.example.service.TemperatureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TemperatureController {

    private final TemperatureService temperatureService;

    @GetMapping("/temperature")
    public City getAverageTemperatureByCity(@RequestParam final String city) {
        return temperatureService.calculateYearlyAverage(city);
    }
}
