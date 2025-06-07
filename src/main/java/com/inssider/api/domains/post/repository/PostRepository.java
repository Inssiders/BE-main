package com.inssider.api.domains.post.repository;

import com.inssider.api.domains.post.dto.PostDTO;
import com.inssider.api.domains.post.dto.PostGetIdResponseDTO;
import com.inssider.api.domains.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

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

  @Query("SELECT new com.inssider.api.domains.post.dto.PostDTO(" +
          "p.account.id, " +
          "p.title, " +
          "(SELECT profile.nickname FROM UserProfile profile WHERE profile.account.id = p.account.id), " +
          "(SELECT profile.profileUrl FROM UserProfile profile WHERE profile.account.id = p.account.id), " +
          "(SELECT COUNT(like) FROM Like like WHERE like.targetId = p.id), " +
          "(SELECT COUNT(comment) FROM Comment comment WHERE comment.post.id = p.id AND comment.isDeleted = false))" +
          "FROM Post p " +
          "WHERE (:keyword IS NULL OR UPPER(p.title) LIKE UPPER(:keyword)) " +
          "AND (:categoryId IS NULL OR p.category.id = :categoryId)")
  Page<PostDTO> findPostsWithSearchDTO(
          Pageable pageable,
          @Param("keyword") String keyword,
          @Param("categoryId") Long categoryId
  );

}
