package com.example._Board.comment.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReplyComment is a Querydsl query type for ReplyComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReplyComment extends EntityPathBase<ReplyComment> {

    private static final long serialVersionUID = -619963142L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReplyComment replyComment1 = new QReplyComment("replyComment1");

    public final com.example._Board.config.QBaseTimeEntity _super = new com.example._Board.config.QBaseTimeEntity(this);

    public final QComment comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath replyComment = createString("replyComment");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example._Board.user.domain.QUser user;

    public QReplyComment(String variable) {
        this(ReplyComment.class, forVariable(variable), INITS);
    }

    public QReplyComment(Path<? extends ReplyComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReplyComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReplyComment(PathMetadata metadata, PathInits inits) {
        this(ReplyComment.class, metadata, inits);
    }

    public QReplyComment(Class<? extends ReplyComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new QComment(forProperty("comment"), inits.get("comment")) : null;
        this.user = inits.isInitialized("user") ? new com.example._Board.user.domain.QUser(forProperty("user")) : null;
    }

}

