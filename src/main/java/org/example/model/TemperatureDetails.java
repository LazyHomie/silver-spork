package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TemperatureDetails {

    private int year;
    private double averageTemperature;
}
