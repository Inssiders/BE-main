package com.inssider.api.domains.tag.service;

import com.inssider.api.domains.tag.entity.Tag;
import com.inssider.api.domains.tag.repository.TagRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;

  @Transactional
  public List<Tag> findOrCreateTags(List<String> tagNames) {
    if (tagNames == null || tagNames.isEmpty()) {
      return new ArrayList<>();
    }

    return tagNames.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(name -> !name.isEmpty())
        .distinct()
        .map(
            tagName -> {
              Optional<Tag> existingTag = tagRepository.findByName(tagName);
              if (existingTag.isPresent()) {
                return existingTag.get();
              } else {
                return tagRepository.save(
                    Tag.builder().name(tagName).created_at(LocalDateTime.now()).build());
              }
            })
        .collect(Collectors.toList());
  }
}
