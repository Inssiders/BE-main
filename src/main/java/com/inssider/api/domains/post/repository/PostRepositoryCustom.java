package com.inssider.api.domains.post.repository;

import com.inssider.api.domains.post.dto.PostCursorRequestDTO;
import com.inssider.api.domains.post.dto.PostCursorResponseDTO;
import com.querydsl.core.BooleanBuilder;

public interface PostRepositoryCustom {
  PostCursorResponseDTO findPostsWithCursor(
      PostCursorRequestDTO requestDTO, BooleanBuilder additionalCondition);

  PostCursorResponseDTO findPostsByAccount(PostCursorRequestDTO requestDTO, Long accountId);

  PostCursorResponseDTO findLikedPostsByAccount(PostCursorRequestDTO requestDTO, Long accountId);
}
