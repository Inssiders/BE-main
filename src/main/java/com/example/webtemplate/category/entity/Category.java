package com.example.webtemplate.category.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "카테고리 이름을 필수로 입력해주세요.")
    @Column(nullable = false)
    private String name;

}
