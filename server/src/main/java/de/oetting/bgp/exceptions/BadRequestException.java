package de.oetting.bgp.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HTTPException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
