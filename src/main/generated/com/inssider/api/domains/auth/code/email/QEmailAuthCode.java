package com.inssider.api.domains.auth.code.email;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmailAuthCode is a Querydsl query type for EmailAuthCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailAuthCode extends EntityPathBase<EmailAuthCode> {

    private static final long serialVersionUID = -478288419L;

    public static final QEmailAuthCode emailAuthCode = new QEmailAuthCode("emailAuthCode");

    public final StringPath code = createString("code");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    public QEmailAuthCode(String variable) {
        super(EmailAuthCode.class, forVariable(variable));
    }

    public QEmailAuthCode(Path<? extends EmailAuthCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmailAuthCode(PathMetadata metadata) {
        super(EmailAuthCode.class, metadata);
    }

}

