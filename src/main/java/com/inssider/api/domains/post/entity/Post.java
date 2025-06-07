package com.inssider.api.domains.post.entity;

import com.inssider.api.common.entity.PostBaseEntity;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.category.entity.Category;
import com.inssider.api.domains.tag.entity.Tag;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends PostBaseEntity {

  @Id @GeneratedValue private Long id;

  @Column(nullable = false, length = 255)
  private String title;

  @Column(name = "media_url", nullable = false, columnDefinition = "TEXT")
  private String mediaUrl;

  @Column(name = "media_upload_time", nullable = false)
  private LocalDateTime mediaUploadTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostTag> postTags = new ArrayList<>();

  public void addTags(List<Tag> tags) {
    if (tags == null || tags.isEmpty()) {
      return;
    }
    List<PostTag> newPostTags =
        tags.stream().map(tag -> new PostTag(this, tag)).collect(Collectors.toList());
    this.postTags.addAll(newPostTags);
  }

  public void updateTitle(String title) {
    this.title = title;
  }

  public void updateContent(String content) {
    this.content = content;
  }

  public void updateMediaUrl(String mediaUrl) {
    this.mediaUrl = mediaUrl;
  }

  public void updateMediaUploadTime(LocalDateTime mediaUploadTime) {
    this.mediaUploadTime = mediaUploadTime;
  }

  public void updateCategory(Category category) {
    this.category = category;
  }
}
