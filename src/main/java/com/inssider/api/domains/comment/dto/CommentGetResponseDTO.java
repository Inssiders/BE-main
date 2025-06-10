package com.inssider.api.domains.comment.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentGetResponseDTO {
  private Long id;
  private String content;
  private String writer;
  private LocalDateTime createdAt;

  @Builder.Default private List<CommentGetResponseDTO> children = new ArrayList<>();

  public void addChild(CommentGetResponseDTO child) {
    this.children.add(child);
  }
}
