package com.inssider.api.domains.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
  private Long id;
  private Long accountId;
  private String title;
  private String nickname;
  private String profileUrl;
  @Builder.Default private Long likeCount = 0L;
  @Builder.Default private Long commentCount = 0L;

  public void updateLikeCount(Long likeCount) {
    this.likeCount = likeCount;
  }

  public void updateCommentCount(Long commentCount) {
    this.commentCount = commentCount;
  }
}
