package com.inssider.api.common.response;

import com.inssider.api.common.ResponseMessage;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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

  public record SearchResponseWrapper<T>(String message, PagedModel<EntityModel<T>> data, PageInfo pageInfo) {}

  public static <T> ResponseEntity<SearchResponseWrapper<T>> of(
          int status,
          PagedModel<EntityModel<T>> data,
          PageInfo pageInfo
  ) {
    HttpStatus httpStatus = HttpStatus.valueOf(status);
    String message = ResponseMessage.of(status).getMessage();
    var body = new SearchResponseWrapper<>(message, data, pageInfo);
    return new ResponseEntity<>(body, httpStatus);
  }

  public static PageInfo createPageInfo(int page, int limit, Long totalElements, int totalPages) {
    return new PageInfo(page, limit, totalElements, totalPages);
  }
}
