package com.example.webtemplate.post.entity.id;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PostTagId implements Serializable {
    private Long postId;
    private Long tagId;
}