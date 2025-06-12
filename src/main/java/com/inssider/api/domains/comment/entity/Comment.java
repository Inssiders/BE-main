package com.inssider.api.domains.comment.entity;

import com.inssider.api.common.entity.PostBaseEntity;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.post.entity.Post;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends PostBaseEntity {

  @Id @GeneratedValue private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_comment_id")
  @Builder.Default
  private Comment parentComment = null;

  @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Comment> childComments = new ArrayList<>();

  public void addChild(Comment child) {
    this.childComments.add(child);
    child.parentComment = this;
  }

  public void updateContent(String content) {
    this.content = content;
  }
}
