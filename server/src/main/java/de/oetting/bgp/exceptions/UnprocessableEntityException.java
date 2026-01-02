package de.oetting.bgp.exceptions;

import org.springframework.http.HttpStatus;

public class UnprocessableEntityException extends HTTPException {

    public UnprocessableEntityException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
