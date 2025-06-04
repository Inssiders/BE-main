package com.example.webtemplate.common;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class CommonControllerAdvice {

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ProblemDetail notFoundHandler(NoSuchElementException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(
        HttpStatus.NOT_FOUND);
    problemDetail.setType(
        Util.buildAbsoluteUri("/error/resource-not-found"));
    problemDetail.setTitle("Resource Not Found");
    problemDetail.setDetail(ex.getMessage());
    return problemDetail;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ProblemDetail badRequestHandler(IllegalArgumentException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(
        HttpStatus.BAD_REQUEST);
    problemDetail.setType(Util.buildAbsoluteUri("/error/invalid-argument"));
    problemDetail.setTitle("Invalid Argument");
    problemDetail.setDetail(ex.getMessage());
    return problemDetail;
  }

}