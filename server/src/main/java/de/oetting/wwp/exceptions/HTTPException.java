package de.oetting.wwp.exceptions;

import org.springframework.http.HttpStatus;

public class HTTPException extends RuntimeException{

    private final HttpStatus httpStatus;

    public HTTPException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
