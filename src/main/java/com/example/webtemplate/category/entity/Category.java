package com.example.webtemplate.category.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Entity
@Getter
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

}
