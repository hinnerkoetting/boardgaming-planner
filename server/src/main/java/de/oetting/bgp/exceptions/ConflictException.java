package de.oetting.bgp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ConflictException extends HTTPException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
