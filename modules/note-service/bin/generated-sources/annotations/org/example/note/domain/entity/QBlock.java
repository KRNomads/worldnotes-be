package org.example.note.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlock is a Querydsl query type for Block
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlock extends EntityPathBase<Block> {

    private static final long serialVersionUID = -562577058L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlock block = new QBlock("block");

    public final MapPath<String, Object, SimplePath<Object>> content = this.<String, Object, SimplePath<Object>>createMap("content", String.class, Object.class, SimplePath.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDefault = createBoolean("isDefault");

    public final QNote note;

    public final NumberPath<Integer> position = createNumber("position", Integer.class);

    public final StringPath title = createString("title");

    public final EnumPath<org.example.note.domain.enums.BlockType> type = createEnum("type", org.example.note.domain.enums.BlockType.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QBlock(String variable) {
        this(Block.class, forVariable(variable), INITS);
    }

    public QBlock(Path<? extends Block> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlock(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlock(PathMetadata metadata, PathInits inits) {
        this(Block.class, metadata, inits);
    }

    public QBlock(Class<? extends Block> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.note = inits.isInitialized("note") ? new QNote(forProperty("note"), inits.get("note")) : null;
    }

}

