package com.inssider.api.domains.comment.repository;

import com.inssider.api.domains.comment.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
