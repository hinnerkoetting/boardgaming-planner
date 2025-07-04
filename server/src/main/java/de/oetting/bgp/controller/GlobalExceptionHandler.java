package de.oetting.bgp.controller;

import de.oetting.bgp.exceptions.HTTPException;
import de.oetting.bgp.infrastructure.HttpErrorResponse;
import org.simpleframework.xml.transform.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    public static final String CLIENT_ERROR = "CLIENT_ERROR";

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public HttpErrorResponse noSuchElementException(NoSuchElementException e) {
        LOG.info("No such element: {}", e.getMessage());
        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle("Not found");
        problem.setType(CLIENT_ERROR);
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
        problem.setType(CLIENT_ERROR);
        return problem;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(AuthorizationDeniedException.class)
    public HttpErrorResponse authorizationDenied(AuthorizationDeniedException e) {
        LOG.info("authorizationDenied: {}", e.getMessage());

        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle(HttpStatus.FORBIDDEN.getReasonPhrase());
        problem.setType(CLIENT_ERROR);
        return problem;
    }

    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public HttpErrorResponse httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        LOG.info("HttpRequestMethodNotSupportedException: {}", e.getMessage());

        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        problem.setType(CLIENT_ERROR);
        return problem;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public HttpErrorResponse notFound(ResourceNotFoundException e) {
        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
        problem.setType(CLIENT_ERROR);
        problem.setDetail("Unknown controller");
        return problem;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public HttpErrorResponse notFound(NoResourceFoundException e) {
        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
        problem.setType(CLIENT_ERROR);
        problem.setDetail("Unknown resource");
        return problem;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public HttpErrorResponse controllerNotFound(NoHandlerFoundException e) {
        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
        problem.setType(CLIENT_ERROR);
        problem.setDetail("Endpoint unknown");
        return problem;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public HttpErrorResponse controllerNotFound(InvalidFormatException e) {
        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problem.setType(CLIENT_ERROR);
        problem.setDetail("Endpoint unknown");
        return problem;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public HttpErrorResponse controllerNotFound(HttpMessageConversionException e) {
        LOG.info("Request could not be parsed: {}", e.getMessage());
        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problem.setType(CLIENT_ERROR);
        problem.setDetail("Request could not be parsed");
        return problem;
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public void ioException(Exception e) {
        // Might be server sent events
        LOG.debug("IOException");
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NumberFormatException.class)
    public void numberFormatException(NumberFormatException e) {
        LOG.info("NumberFormatException {}", e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void methodArgumentTypeMismatchException(NumberFormatException e) {
        LOG.info("MethodArgumentTypeMismatchException {}", e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public HttpErrorResponse otherError(Exception e) {
        LOG.error("Exception: ", e);

        HttpErrorResponse problem = new HttpErrorResponse();
        problem.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        problem.setType("SERVER_ERROR");
        return problem;
    }
}
