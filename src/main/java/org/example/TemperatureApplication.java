package org.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TemperatureApplication {
//Welcome in the recruitment challenge.
// Write an application that, at the endpoint specified by you,
// returns the yearly average temperatures for a given city in the
// format array of objects with the following fields: year, averageTemperature.

//CSV file with data is no less than 3GB in size.
//The file represents temperature measurements in the format city;yyyy-mm-dd HH:mm:ss.SSS;temp
//The content of the source file may change during the application's running
    public static void main(String[] args) {
        SpringApplication.run(TemperatureApplication.class, args);
    }
}