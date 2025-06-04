package com.example.webtemplate.tag.repository;

import com.example.webtemplate.tag.entity.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

  Optional<Tag> findByName(String tagName);
}
