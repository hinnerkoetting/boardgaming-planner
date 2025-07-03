package de.oetting.wwp.exceptions;

import org.springframework.http.HttpStatus;

public class UnprocessableEntityException extends HTTPException {

    public UnprocessableEntityException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
