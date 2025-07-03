package de.oetting.wwp.controller;

import de.oetting.wwp.exceptions.HTTPException;
import de.oetting.wwp.infrastructure.HttpErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public HttpErrorResponse noSuchElementException(NoSuchElementException e) {
        LOG.info("No such element: {}", e.getMessage());
        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle("Not found");
        problem.setType("CLIENT_ERROR");
        problem.setDetail(e.getMessage());
        return problem;
    }

    @ExceptionHandler(HTTPException.class)
    public ResponseEntity<HttpErrorResponse> httpException(HTTPException e) {
        LOG.info("HTTP Status {}: {}", e.getHttpStatus(), e.getMessage());
        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle(e.getHttpStatus().getReasonPhrase());
        problem.setType(e.getHttpStatus().getReasonPhrase());
        problem.setDetail(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus())
                .body(problem);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public HttpErrorResponse conflict(DataIntegrityViolationException e) {
        LOG.info("DataIntegrityViolationException: {}", e.getMessage());

        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle(HttpStatus.CONFLICT.getReasonPhrase());
        problem.setType("CLIENT_ERROR");
        return problem;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(AuthorizationDeniedException.class)
    public HttpErrorResponse authorizationDenied(AuthorizationDeniedException e) {
        LOG.info("authorizationDenied: {}", e.getMessage());

        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle(HttpStatus.FORBIDDEN.getReasonPhrase());
        problem.setType("CLIENT_ERROR");
        return problem;
    }

    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public HttpErrorResponse httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        LOG.info("HttpRequestMethodNotSupportedException: {}", e.getMessage());

        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        problem.setType("CLIENT_ERROR");
        return problem;
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public HttpErrorResponse otherError(Exception e) {
        LOG.info("Exception: Type: {}, Message: {}", e.getClass().getSimpleName(), e.getMessage());

        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        problem.setType("SERVER_ERROR");
        return problem;
    }
}
