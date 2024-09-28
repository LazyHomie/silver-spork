package org.example.validator;

import org.example.exceptions.FileValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import static org.example.exceptions.ErrorMessages.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvValidatorTest {

    private final CsvValidator csvValidator = new CsvValidator();

    @TempDir
    Path tempDir;

    @Test
    void validateFileSize_shouldThrowExceptionWhenFileSizeIsTooLow() throws IOException {
        // given
        Path tempFile = Files.createFile(tempDir.resolve("test.csv"));
        Files.write(tempFile, "Test data".getBytes());

        // when
        FileValidationException exception = assertThrows(FileValidationException.class, () ->
                csvValidator.validateFileSize(tempFile.toString())
        );

        // then
        assert (exception.getErrorMessage().contains(FILE_SIZE_TOO_LOW_EXCEPTION));
    }

    @Test
    void validateCSVLineFormat_shouldThrowExceptionWhenLineIsInvalid() {
        // given
        String invalidLine = "Warszawa;2023-09-01 00:00:00.000";

        // when
        FileValidationException exception = assertThrows(FileValidationException.class, () ->
                csvValidator.validateCSVLineFormat(invalidLine)
        );

        // then
        assert (exception.getErrorMessage().contains("Invalid line format"));
    }

    @Test
    void validateCSVLineFormat_shouldThrowExceptionWhenTemperatureIsInvalid() {
        // given
        String invalidLine = "Warszawa;2023-09-01 00:00:00.000;invalidTemp";

        // when
        FileValidationException exception = assertThrows(FileValidationException.class, () ->
                csvValidator.validateCSVLineFormat(invalidLine)
        );

        // then
        assert (exception.getErrorMessage().contains("Invalid temperature format"));
    }

    @Test
    void validateCSVLineFormat_shouldThrowExceptionWhenDateIsInvalid() {
        // given
        String invalidLine = "Warszawa;invalid-date;9.97";

        // when
        FileValidationException exception = assertThrows(FileValidationException.class, () ->
                csvValidator.validateCSVLineFormat(invalidLine)
        );

        // then
        assertTrue(exception.getErrorMessage().contains("Invalid date format"));
    }

    @Test
    void validateFileModification_shouldThrowExceptionWhenFileModified() throws IOException {
        // given
        Path tempFile = Files.createFile(tempDir.resolve("test.csv"));
        FileTime initialTime = Files.getLastModifiedTime(tempFile);
        csvValidator.validateFileModification(tempFile.toString());
        Files.setLastModifiedTime(tempFile, FileTime.fromMillis(initialTime.toMillis() + 1000));

        // when
        FileValidationException exception = assertThrows(FileValidationException.class, () ->
                csvValidator.validateFileModification(tempFile.toString())
        );

        // then
        assertTrue(exception.getErrorMessage().contains(FILE_INTEGRITY_EXCEPTION));
    }

    @Test
    void validateFileModification_shouldThrowExceptionWhenIOExceptionOccurs() {
        // given
        Path tempFile = tempDir.resolve("non-existent-file.csv");

        // when
        FileValidationException exception = assertThrows(FileValidationException.class, () ->
                csvValidator.validateFileModification(tempFile.toString())
        );

        // then
        assertTrue(exception.getErrorMessage().contains(FILE_MODIFICATION_TIME_EXCEPTION));
    }
}