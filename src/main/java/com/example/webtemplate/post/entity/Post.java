package com.example.webtemplate.post.entity;

import com.example.webtemplate.account.Account;
import com.example.webtemplate.category.entity.Category;
import com.example.webtemplate.common.entity.ContentBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "posts")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends ContentBaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "제목을 입력해주세요.")
    @Column(nullable = false, length = 255)
    private String title;

    @NotNull(message = "밈 url을 입력해주세요.")
    @Column(nullable = false)
    private String media_url;

    @NotNull(message = "밈 업로드 시간을 입력해주세요.")
    @Column(nullable = false)
    private LocalDateTime media_upload_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
