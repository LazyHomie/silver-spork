package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.CityNotFoundException;
import org.example.exceptions.FileProcessingException;
import org.example.model.City;
import org.example.model.TemperatureDetails;
import org.example.validator.CsvValidator;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.example.exceptions.ErrorMessages.CITY_NOT_FOUND_EXCEPTION;
import static org.example.exceptions.ErrorMessages.FILE_PROCESSING_EXCEPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemperatureService {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final CsvValidator csvValidator = new CsvValidator();
    private final ReaderProvider readerProvider;

    public City calculateYearlyAverage(String cityName) {
        String filePath = "src/main/java/org/example/data/example_file.csv";

        csvValidator.validateFileSize(filePath);
//        csvValidator.validateFileModification(filePath);

        Map<Integer, List<Double>> yearlyTemperatures = new HashMap<>();

        lock.readLock().lock();

        try (BufferedReader reader = readerProvider.getReader(Path.of(filePath))) {
            reader.lines().forEach(line -> handleTemperatureCalculations(cityName, line, yearlyTemperatures));
        } catch (IOException e) {
            throw new FileProcessingException(FILE_PROCESSING_EXCEPTION + filePath, e);
        } finally {
            lock.readLock().unlock();
        }

        if (yearlyTemperatures.isEmpty()) {
            throw new CityNotFoundException(CITY_NOT_FOUND_EXCEPTION + cityName);
        }

        List<TemperatureDetails> temperatureDetailsList = yearlyTemperatures.entrySet().stream()
                .map(TemperatureService::generateTemperatureDetails)
                .sorted(Comparator.comparingInt(TemperatureDetails::getYear))
                .toList();

        return new City(cityName, temperatureDetailsList);
    }

    private void handleTemperatureCalculations(String cityName, String line, Map<Integer, List<Double>> yearlyTemperatures) {
        csvValidator.validateCSVLineFormat(line);
        String[] parts = line.split(";");
        if (parts.length == 3 && parts[0].equalsIgnoreCase(cityName)) {
            String date = parts[1];
            Double temperature = Double.valueOf(parts[2]);

            int year = Integer.parseInt(date.substring(0, 4));
            yearlyTemperatures.computeIfAbsent(year, k -> new ArrayList<>()).add(temperature);
        }
    }

    private static TemperatureDetails generateTemperatureDetails(Map.Entry<Integer, List<Double>> entry) {
        final int year = entry.getKey();
        final List<Double> temps = entry.getValue();
        final double avgTemp = temps.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return new TemperatureDetails(year, avgTemp);
    }
}
