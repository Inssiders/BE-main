package com.inssider.api.domains.post.repository;

import com.inssider.api.domains.post.dto.PostGetIdResponseDTO;
import com.inssider.api.domains.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

  @Query(
      "SELECT p FROM Post p "
          + "LEFT JOIN FETCH p.postTags pt "
          + "LEFT JOIN FETCH pt.tag "
          + "WHERE p.id = :id")
  Optional<Post> findByIdWithTag(@Param("id") Long postId);

  @Query(
      "SELECT new com.inssider.api.domains.post.dto.PostGetIdResponseDTO(p.id, p.createdAt) "
          + "FROM Post p WHERE p.createdAt >= :since")
  List<PostGetIdResponseDTO> findPostsByCreatedAtAfter(@Param("since") LocalDateTime since);

  @Query(
      "SELECT new com.inssider.api.domains.post.dto.PostGetIdResponseDTO(p.id, p.createdAt) FROM Post p")
  List<PostGetIdResponseDTO> findAllIds();
}
