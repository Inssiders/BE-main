package com.example.webtemplate.tag.repository;

import com.example.webtemplate.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
