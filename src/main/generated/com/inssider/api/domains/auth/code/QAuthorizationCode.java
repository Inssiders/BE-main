package com.inssider.api.domains.auth.code;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuthorizationCode is a Querydsl query type for AuthorizationCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthorizationCode extends EntityPathBase<AuthorizationCode> {

    private static final long serialVersionUID = 1800441540L;

    public static final QAuthorizationCode authorizationCode = new QAuthorizationCode("authorizationCode");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public QAuthorizationCode(String variable) {
        super(AuthorizationCode.class, forVariable(variable));
    }

    public QAuthorizationCode(Path<? extends AuthorizationCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthorizationCode(PathMetadata metadata) {
        super(AuthorizationCode.class, metadata);
    }

}

