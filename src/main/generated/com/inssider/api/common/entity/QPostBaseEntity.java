package com.inssider.api.common.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPostBaseEntity is a Querydsl query type for PostBaseEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QPostBaseEntity extends EntityPathBase<PostBaseEntity> {

    private static final long serialVersionUID = -790832444L;

    public static final QPostBaseEntity postBaseEntity = new QPostBaseEntity("postBaseEntity");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QPostBaseEntity(String variable) {
        super(PostBaseEntity.class, forVariable(variable));
    }

    public QPostBaseEntity(Path<? extends PostBaseEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPostBaseEntity(PathMetadata metadata) {
        super(PostBaseEntity.class, metadata);
    }

}

