package com.example.webtemplate.post.repository;

import com.example.webtemplate.post.dto.PostGetIdResponseDTO;
import com.example.webtemplate.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.postTags pt " +
            "LEFT JOIN FETCH pt.tag " +
            "WHERE p.id = :id")
    Optional<Post> findByIdWithTag(@Param("id") Long postId);

    @Query("SELECT new com.example.webtemplate.post.dto.PostGetIdResponseDTO(p.id, p.createdAt) " +
            "FROM Post p WHERE p.createdAt >= :since")
    List<PostGetIdResponseDTO> findPostsByCreatedAtAfter(@Param("since") LocalDateTime since);

    @Query("SELECT new com.example.webtemplate.post.dto.PostGetIdResponseDTO(p.id, p.createdAt) FROM Post p")
    List<PostGetIdResponseDTO> findAllIds();
}
