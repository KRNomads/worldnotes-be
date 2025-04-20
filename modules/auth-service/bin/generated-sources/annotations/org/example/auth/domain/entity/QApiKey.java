package org.example.auth.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QApiKey is a Querydsl query type for ApiKey
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApiKey extends EntityPathBase<ApiKey> {

    private static final long serialVersionUID = -1272480962L;

    public static final QApiKey apiKey = new QApiKey("apiKey");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final BooleanPath enabled = createBoolean("enabled");

    public final DateTimePath<java.time.LocalDateTime> expiresAt = createDateTime("expiresAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath key = createString("key");

    public final ComparablePath<java.util.UUID> userId = createComparable("userId", java.util.UUID.class);

    public QApiKey(String variable) {
        super(ApiKey.class, forVariable(variable));
    }

    public QApiKey(Path<? extends ApiKey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QApiKey(PathMetadata metadata) {
        super(ApiKey.class, metadata);
    }

}

