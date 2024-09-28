package org.example.exceptions;

import org.springframework.http.HttpStatus;

public class CityNotFoundException extends BaseCustomException {

    public CityNotFoundException(String cityName) {
        super(cityName, HttpStatus.NOT_FOUND);
    }
}
