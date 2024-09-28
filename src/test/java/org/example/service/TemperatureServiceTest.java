package org.example.service;

import org.example.exceptions.CityNotFoundException;
import org.example.exceptions.FileProcessingException;
import org.example.model.City;
import org.example.validator.CsvValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.example.exceptions.ErrorMessages.CITY_NOT_FOUND_EXCEPTION;
import static org.example.exceptions.ErrorMessages.FILE_PROCESSING_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class TemperatureServiceTest {

    @Mock
    private CsvValidator csvValidator;

    @Mock
    private ReaderProvider readerProvider;

    @InjectMocks
    private TemperatureService temperatureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateYearlyAverage_shouldThrowCityNotFoundException() throws IOException {
        // given
        String city = "UnknownCity";
        BufferedReader reader = new BufferedReader(new StringReader(""));

        // when
        doNothing().when(csvValidator).validateFileSize(anyString());
        doNothing().when(csvValidator).validateFileModification(anyString());
        when(readerProvider.getReader(any(Path.class))).thenReturn(reader);
        // then
        CityNotFoundException exception = assertThrows(CityNotFoundException.class, () ->
                temperatureService.calculateYearlyAverage(city)
        );
        assertTrue(exception.getErrorMessage().contains(CITY_NOT_FOUND_EXCEPTION));
    }

    @Test
    void calculateYearlyAverage_shouldCalculateCorrectAverages() throws Exception {
        // given
        String city = "Warszawa";
        String csvData = "Warszawa;2019-09-19 05:17:32.619;9.97\n" +
                "Warszawa;2020-09-19 05:17:32.619;10.0";
        BufferedReader reader = new BufferedReader(new StringReader(csvData));
        String filePath = "src/main/java/org/example/data/example_file.csv";

        // when
        doNothing().when(csvValidator).validateFileSize(anyString());
        doNothing().when(csvValidator).validateFileModification(anyString());
        doNothing().when(csvValidator).validateCSVLineFormat(anyString());
        when(readerProvider.getReader(Paths.get(filePath))).thenReturn(reader);

        // then
        City result = temperatureService.calculateYearlyAverage(city);

        assertEquals(city, result.getName());
        assertEquals(2, result.getTemperatureDetailsList().size());
        assertEquals(9.97, result.getTemperatureDetailsList().get(0).getAverageTemperature());
        assertEquals(10.0, result.getTemperatureDetailsList().get(1).getAverageTemperature());
    }

    @Test
    void calculateYearlyAverage_shouldThrowFileProcessingExceptionWhenIOExceptionOccurs() throws IOException {
        // given
        String city = "Warszawa";

        // when
        doNothing().when(csvValidator).validateFileSize(anyString());
        doNothing().when(csvValidator).validateFileModification(anyString());
        when(readerProvider.getReader(any(Path.class))).thenThrow(new IOException());

        // then
        FileProcessingException exception = assertThrows(FileProcessingException.class, () ->
                temperatureService.calculateYearlyAverage(city)
        );
        assertTrue(exception.getErrorMessage().contains(FILE_PROCESSING_EXCEPTION));
    }
}