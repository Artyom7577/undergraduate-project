package com.artyom.undergraduateproject.advice;

import java.net.URI;

import com.artyom.undergraduateproject.exceptions.ApplicationExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ApplicationExceptions.class)
    public ProblemDetail handleException(ApplicationExceptions ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setType(URI.create("https://example.com/problems/bad-request"));
        problem.setTitle("bad request");
        return problem;
    }
}
