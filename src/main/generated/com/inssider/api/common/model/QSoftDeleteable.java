package com.inssider.api.common.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSoftDeleteable is a Querydsl query type for SoftDeleteable
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QSoftDeleteable extends EntityPathBase<SoftDeleteable> {

    private static final long serialVersionUID = 1392019407L;

    public static final QSoftDeleteable softDeleteable = new QSoftDeleteable("softDeleteable");

    public final QAuditable _super = new QAuditable(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSoftDeleteable(String variable) {
        super(SoftDeleteable.class, forVariable(variable));
    }

    public QSoftDeleteable(Path<? extends SoftDeleteable> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSoftDeleteable(PathMetadata metadata) {
        super(SoftDeleteable.class, metadata);
    }

}

