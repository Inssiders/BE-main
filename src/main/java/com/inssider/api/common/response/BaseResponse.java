package com.inssider.api.common.response;

import com.inssider.api.common.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseResponse<T> {
  public record ResponseWrapper<T>(String message, T data) {}

  public static <T> ResponseEntity<ResponseWrapper<T>> of(int status, T data) {
    HttpStatus httpStatus = HttpStatus.valueOf(status);
    String message = ResponseMessage.of(status).getMessage();
    var body = new ResponseWrapper<>(message, data);
    return new ResponseEntity<>(body, httpStatus);
  }
}
