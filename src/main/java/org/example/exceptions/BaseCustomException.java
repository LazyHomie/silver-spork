package org.example.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class BaseCustomException extends RuntimeException {

    private final String errorMessage;
    private final HttpStatus httpStatus;
}

