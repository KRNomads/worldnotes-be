package org.example.user.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserSocialToken is a Querydsl query type for UserSocialToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserSocialToken extends EntityPathBase<UserSocialToken> {

    private static final long serialVersionUID = -269946005L;

    public static final QUserSocialToken userSocialToken = new QUserSocialToken("userSocialToken");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath refreshToken = createString("refreshToken");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final ComparablePath<java.util.UUID> userId = createComparable("userId", java.util.UUID.class);

    public QUserSocialToken(String variable) {
        super(UserSocialToken.class, forVariable(variable));
    }

    public QUserSocialToken(Path<? extends UserSocialToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserSocialToken(PathMetadata metadata) {
        super(UserSocialToken.class, metadata);
    }

}

