package com.inssider.api.domains.post.repository;

import com.inssider.api.domains.post.dto.PostCursorRequestDTO;
import com.inssider.api.domains.post.dto.PostCursorResponseDTO;

public interface PostRepositoryCustom {
    PostCursorResponseDTO findPostsWithCursor(PostCursorRequestDTO requestDTO);
}
