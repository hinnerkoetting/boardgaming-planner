package de.oetting.wwp.controller;

import de.oetting.wwp.infrastructure.RestProblem;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public RestProblem noSuchElementException(NoSuchElementException e) {

        RestProblem problem = new RestProblem();
        problem.setTitle("Not found");
        problem.setType("NOT_FOUND");
        return problem;
    }
}
