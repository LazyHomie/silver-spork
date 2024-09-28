package org.example.validator;

import org.example.exceptions.FileValidationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.example.exceptions.ErrorMessages.*;

public class CsvValidator {

    private static final long MIN_FILE_SIZE = 100;
    private FileTime lastModifiedTime;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public void validateFileSize(String filePath) {
        File csvFile = new File(filePath);
        if (csvFile.length() < MIN_FILE_SIZE) {
            throw new FileValidationException(FILE_SIZE_TOO_LOW_EXCEPTION);
        }
    }

    public void validateFileModification(String filePath) {
        try {
            FileTime currentModifiedTime = Files.getLastModifiedTime(Paths.get(filePath));
            if (lastModifiedTime != null && !lastModifiedTime.equals(currentModifiedTime)) {
                throw new FileValidationException(FILE_INTEGRITY_EXCEPTION);
            }
            lastModifiedTime = currentModifiedTime;
        } catch (IOException e) {
            throw new FileValidationException(FILE_MODIFICATION_TIME_EXCEPTION);
        }
    }

    public void validateCSVLineFormat(String line) {
        String[] parts = line.split(";");
        if (parts.length != 3) {
            throw new FileValidationException(String.format(FILE_FORMAT_EXCEPTION, "Invalid line format"));
        }

        try {
            dateFormat.parse(parts[1]);
        } catch (ParseException e) {
            throw new FileValidationException(String.format(FILE_FORMAT_EXCEPTION, "Invalid date format"));
        }

        try {
            Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            throw new FileValidationException(String.format(FILE_FORMAT_EXCEPTION, "Invalid temperature format"));
        }
    }
}
