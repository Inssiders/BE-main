package com.inssider.api.domains.post.repository;

import com.inssider.api.domains.comment.entity.QComment;
import com.inssider.api.domains.like.entity.QLike;
import com.inssider.api.domains.post.dto.PostCursorRequestDTO;
import com.inssider.api.domains.post.dto.PostCursorResponseDTO;
import com.inssider.api.domains.post.dto.PostDTO;
import com.inssider.api.domains.post.entity.QPost;
import com.inssider.api.domains.profile.QUserProfile;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public PostCursorResponseDTO findPostsByAccount(PostCursorRequestDTO requestDTO, Long accountId) {
    BooleanBuilder postFilter = new BooleanBuilder();
    postFilter.and(QPost.post.account.id.eq(accountId));

    return findPostsWithCursor(requestDTO, postFilter);
  }

  @Override
  public PostCursorResponseDTO findLikedPostsByAccount(
      PostCursorRequestDTO requestDTO, Long accountId) {
    QPost post = QPost.post;
    QLike like = QLike.like;

    List<Long> likedPostIds =
        queryFactory.select(like.targetId).from(like).where(like.account.id.eq(accountId)).fetch();

    if (likedPostIds.isEmpty()) {
      return PostCursorResponseDTO.builder()
          .content(List.of())
          .hasNext(false)
          .nextCursor(null)
          .build();
    }

    BooleanBuilder likedFilter = new BooleanBuilder();
    likedFilter.and(post.id.in(likedPostIds));

    return findPostsWithCursor(requestDTO, likedFilter);
  }

  @Override
  public PostCursorResponseDTO findPostsWithCursor(
      PostCursorRequestDTO requestDTO, BooleanBuilder filter) {
    QPost post = QPost.post;
    QUserProfile profile = QUserProfile.userProfile;
    QLike like = QLike.like;
    QComment comment = QComment.comment;

    BooleanBuilder where = new BooleanBuilder();

    if (requestDTO.getLast_id() != null) {
      where.and(post.id.lt(requestDTO.getLast_id()));
    }

    if (requestDTO.getKeyword() != null && !requestDTO.getKeyword().trim().isEmpty()) {
      where.and(post.title.containsIgnoreCase(requestDTO.getKeyword()));
    }

    if (requestDTO.getCategory_id() != null) {
      where.and(post.category.id.eq(requestDTO.getCategory_id()));
    }

    if (filter != null) {
      where.and(filter);
    }

    List<PostDTO> posts =
        queryFactory
            .select(post.id, post.account.id, post.title, profile.nickname, profile.profileUrl)
            .from(post)
            .leftJoin(profile)
            .on(profile.account.id.eq(post.account.id))
            .where(where)
            .orderBy(post.createdAt.desc(), post.id.desc())
            .limit(requestDTO.getSize() + 1)
            .fetch()
            .stream()
            .map(
                entity ->
                    PostDTO.builder()
                        .id(entity.get(post.id))
                        .accountId(entity.get(post.account.id))
                        .title(entity.get(post.title))
                        .nickname(entity.get(profile.nickname))
                        .profileUrl(entity.get(profile.profileUrl))
                        .likeCount(0L)
                        .commentCount(0L)
                        .build())
            .collect(Collectors.toList());

    boolean hasNext = posts.size() > requestDTO.getSize();
    if (hasNext) {
      posts.remove(posts.size() - 1);
    }

    if (!posts.isEmpty()) {
      List<Long> postIds = posts.stream().map(PostDTO::getId).collect(Collectors.toList());

      Map<Long, Long> likeCountMap =
          queryFactory
              .select(like.targetId, like.count())
              .from(like)
              .where(like.targetId.in(postIds))
              .groupBy(like.targetId)
              .fetch()
              .stream()
              .collect(
                  Collectors.toMap(obj -> obj.get(like.targetId), obj -> obj.get(like.count())));

      Map<Long, Long> commentCountMap =
          queryFactory
              .select(comment.post.id, comment.count())
              .from(comment)
              .where(comment.post.id.in(postIds).and(comment.isDeleted.eq(false)))
              .groupBy(comment.post.id)
              .fetch()
              .stream()
              .collect(
                  Collectors.toMap(
                      obj -> obj.get(comment.post.id), obj -> obj.get(comment.count())));

      posts.forEach(
          postDTO -> {
            postDTO.updateLikeCount(likeCountMap.getOrDefault(postDTO.getId(), 0L));
            postDTO.updateCommentCount(commentCountMap.getOrDefault(postDTO.getId(), 0L));
          });
    }

    Long nextCursor = hasNext && !posts.isEmpty() ? posts.get(posts.size() - 1).getId() : null;

    return PostCursorResponseDTO.builder()
        .content(posts)
        .hasNext(hasNext)
        .nextCursor(nextCursor)
        .build();
  }
}
