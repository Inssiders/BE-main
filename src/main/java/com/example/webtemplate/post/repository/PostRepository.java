package com.example.webtemplate.post.repository;

import com.example.webtemplate.category.entity.Category;
import com.example.webtemplate.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

}
