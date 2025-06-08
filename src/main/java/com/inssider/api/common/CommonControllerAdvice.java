package com.inssider.api.common;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
class CommonControllerAdvice {

  @ExceptionHandler({NoSuchElementException.class, NoResourceFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ProblemDetail notFoundHandler(NoSuchElementException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problemDetail.setType(Util.buildAbsoluteUri("/error/resource-not-found"));
    problemDetail.setTitle("Resource Not Found");
    problemDetail.setDetail(ex.getMessage());
    return problemDetail;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ProblemDetail badRequestHandler(IllegalArgumentException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setType(Util.buildAbsoluteUri("/error/invalid-argument"));
    problemDetail.setTitle("Invalid Argument");
    problemDetail.setDetail(ex.getMessage());
    return problemDetail;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ProblemDetail generalExceptionHandler(Exception ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    problemDetail.setType(Util.buildAbsoluteUri("/error/internal-server-error"));
    problemDetail.setTitle(ex.getClass().getSimpleName());
    problemDetail.setDetail(ex.getMessage());
    return problemDetail;
  }

  @ExceptionHandler(IllegalStateException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ProblemDetail conflictHandler(IllegalStateException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    problemDetail.setType(Util.buildAbsoluteUri("/error/conflict"));
    problemDetail.setTitle("Conflict");
    problemDetail.setDetail(ex.getMessage());
    return problemDetail;
  }
}
