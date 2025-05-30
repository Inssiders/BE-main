package com.example.webtemplate.post.entity;

import com.example.webtemplate.post.entity.id.PostTagId;
import com.example.webtemplate.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "post_tags")
public class PostTag {

    @EmbeddedId
    private PostTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @MapsId("postId")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    @MapsId("tagId")
    private Tag tag;

}