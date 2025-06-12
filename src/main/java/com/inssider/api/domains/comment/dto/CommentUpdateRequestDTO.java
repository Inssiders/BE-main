package com.inssider.api.domains.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentUpdateRequestDTO {
  @NotBlank(message = "내용을 입력해주세요.")
  private String content;
}
