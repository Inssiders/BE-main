package com.inssider.api.domains.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCursorRequestDTO {
  @JsonProperty("last_id") // 바인딩 되지 않음
  private Long last_id;

  private Integer size = 10;

  @JsonProperty("profile_filter")
  private String profile_filter;

  private String keyword;

  @JsonProperty("category_id")
  private Long category_id;
}
