package com.example._Board.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDeleteUser is a Querydsl query type for DeleteUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeleteUser extends EntityPathBase<DeleteUser> {

    private static final long serialVersionUID = 1314346003L;

    public static final QDeleteUser deleteUser = new QDeleteUser("deleteUser");

    public final DateTimePath<java.time.LocalDateTime> deleteAt = createDateTime("deleteAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath loginId = createString("loginId");

    public final StringPath name = createString("name");

    public final StringPath role = createString("role");

    public QDeleteUser(String variable) {
        super(DeleteUser.class, forVariable(variable));
    }

    public QDeleteUser(Path<? extends DeleteUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDeleteUser(PathMetadata metadata) {
        super(DeleteUser.class, metadata);
    }

}

