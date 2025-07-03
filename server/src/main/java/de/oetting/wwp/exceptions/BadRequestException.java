package de.oetting.wwp.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException  extends HTTPException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
