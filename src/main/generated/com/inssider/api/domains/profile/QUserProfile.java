package com.inssider.api.domains.profile;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserProfile is a Querydsl query type for UserProfile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserProfile extends EntityPathBase<UserProfile> {

    private static final long serialVersionUID = 2044959506L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserProfile userProfile = new QUserProfile("userProfile");

    public final com.inssider.api.common.model.QAuditable _super = new com.inssider.api.common.model.QAuditable(this);

    public final com.inssider.api.domains.account.QAccount account;

    public final BooleanPath accountVisible = createBoolean("accountVisible");

    public final StringPath bio = createString("bio");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath followerVisible = createBoolean("followerVisible");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath profileUrl = createString("profileUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUserProfile(String variable) {
        this(UserProfile.class, forVariable(variable), INITS);
    }

    public QUserProfile(Path<? extends UserProfile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserProfile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserProfile(PathMetadata metadata, PathInits inits) {
        this(UserProfile.class, metadata, inits);
    }

    public QUserProfile(Class<? extends UserProfile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.inssider.api.domains.account.QAccount(forProperty("account"), inits.get("account")) : null;
    }

}

