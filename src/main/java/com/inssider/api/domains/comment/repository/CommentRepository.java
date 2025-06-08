package com.inssider.api.domains.comment.repository;

import com.inssider.api.domains.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Query(
      "SELECT c FROM Comment c "
          + "LEFT JOIN FETCH c.account a "
          + "LEFT JOIN FETCH a.profile "
          + "WHERE c.post.id = :postId ")
  List<Comment> findAllByPostIdWithAccount(@Param("postId") Long postId);
}
