package com.example.webtemplate.post.repository;

import com.example.webtemplate.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
