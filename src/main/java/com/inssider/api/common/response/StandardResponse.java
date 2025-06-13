package com.inssider.api.common.response;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class StandardResponse {

  public record GetIndexResponse(List<Long> index) {}

  public record Pagination(
      int limit,
      long totalItems,
      int totalPages,
      int currentPage,
      boolean hasNext,
      boolean hasPrev) {}

  public record SortRes(String field, String direction) {}

  public record QueryResponse<T>(List<T> items, Pagination pagination, List<SortRes> sorts) {

    public static <T> QueryResponse<T> of(List<T> items, Page<T> pageResult, int limit) {
      Pagination pagination =
          new Pagination(
              limit,
              pageResult.getTotalElements(),
              pageResult.getTotalPages(),
              pageResult.getNumber(),
              pageResult.hasNext(),
              pageResult.hasPrevious());

      List<SortRes> sorts =
          pageResult.getSort().stream()
              .map(order -> new SortRes(order.getProperty(), order.getDirection().name()))
              .collect(Collectors.toList());

      return new QueryResponse<>(items, pagination, sorts);
    }
  }
}
