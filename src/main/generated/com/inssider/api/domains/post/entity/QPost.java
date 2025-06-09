package com.inssider.api.domains.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 904079780L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.inssider.api.common.entity.QPostBaseEntity _super = new com.inssider.api.common.entity.QPostBaseEntity(this);

    public final com.inssider.api.domains.account.QAccount account;

    public final com.inssider.api.domains.category.entity.QCategory category;

    //inherited
    public final StringPath content = _super.content;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final DateTimePath<java.time.LocalDateTime> mediaUploadTime = createDateTime("mediaUploadTime", java.time.LocalDateTime.class);

    public final StringPath mediaUrl = createString("mediaUrl");

    public final ListPath<PostTag, QPostTag> postTags = this.<PostTag, QPostTag>createList("postTags", PostTag.class, QPostTag.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.inssider.api.domains.account.QAccount(forProperty("account"), inits.get("account")) : null;
        this.category = inits.isInitialized("category") ? new com.inssider.api.domains.category.entity.QCategory(forProperty("category")) : null;
    }

}

