package org.example.exceptions;

import org.springframework.http.HttpStatus;

public class FileValidationException extends BaseCustomException {

    public FileValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
