package com.inssider.api.domains.comment.mapper;

import com.inssider.api.domains.comment.dto.CommentCreateResponseDTO;
import com.inssider.api.domains.comment.entity.Comment;
import com.inssider.api.domains.post.dto.PostResponseDTO;
import com.inssider.api.domains.post.entity.Post;

import java.util.List;

public class CommentMapper {

    public static CommentCreateResponseDTO toDTO(Comment comment) {

        return CommentCreateResponseDTO.builder()
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
