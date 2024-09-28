package org.example.exceptions;

import org.springframework.http.HttpStatus;

public class FileProcessingException extends BaseCustomException {

    public FileProcessingException(String message, Throwable cause) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
        initCause(cause);
    }
}
