package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class City {

    private String name;
    private List<TemperatureDetails> temperatureDetailsList;
}
