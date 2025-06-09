package com.inssider.api.domains.account;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccount is a Querydsl query type for Account
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccount extends EntityPathBase<Account> {

    private static final long serialVersionUID = -1568083867L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccount account = new QAccount("account");

    public final com.inssider.api.common.model.QSoftDeleteable _super = new com.inssider.api.common.model.QSoftDeleteable(this);

    public final EnumPath<AccountDataTypes.AccountType> accountType = createEnum("accountType", AccountDataTypes.AccountType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final StringPath password = createString("password");

    public final com.inssider.api.domains.profile.QUserProfile profile;

    public final StringPath providerUserId = createString("providerUserId");

    public final com.inssider.api.domains.auth.token.refresh.QRefreshToken refreshToken;

    public final EnumPath<AccountDataTypes.RoleType> role = createEnum("role", AccountDataTypes.RoleType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAccount(String variable) {
        this(Account.class, forVariable(variable), INITS);
    }

    public QAccount(Path<? extends Account> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccount(PathMetadata metadata, PathInits inits) {
        this(Account.class, metadata, inits);
    }

    public QAccount(Class<? extends Account> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.profile = inits.isInitialized("profile") ? new com.inssider.api.domains.profile.QUserProfile(forProperty("profile"), inits.get("profile")) : null;
        this.refreshToken = inits.isInitialized("refreshToken") ? new com.inssider.api.domains.auth.token.refresh.QRefreshToken(forProperty("refreshToken"), inits.get("refreshToken")) : null;
    }

}

