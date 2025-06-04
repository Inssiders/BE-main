package com.example.webtemplate.like.repository;

import com.example.webtemplate.like.LikeTargetType;
import com.example.webtemplate.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

  boolean existsByAccountIdAndTargetTypeAndTargetId(
      Long accountId, LikeTargetType targetType, Long targetId);

  void deleteByAccountIdAndTargetTypeAndTargetId(
      Long accountId, LikeTargetType targetType, Long targetId);

  int countByTargetTypeAndTargetId(LikeTargetType targetType, Long targetId);
}
