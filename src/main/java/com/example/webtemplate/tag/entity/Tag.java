package com.example.webtemplate.tag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "태그명을 필수로 입력해주세요.")
    @Column(nullable = false)
    private String name;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

}
