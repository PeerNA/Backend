package cos.peerna.domain.reply.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReply is a Querydsl query type for Reply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReply extends EntityPathBase<Reply> {

    private static final long serialVersionUID = 690141417L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReply reply = new QReply("reply");

    public final StringPath answer = createString("answer");

    public final cos.peerna.domain.history.model.QHistory history;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRequested = createBoolean("isRequested");

    public final NumberPath<Long> likeCount = createNumber("likeCount", Long.class);

    public final ListPath<Likey, QLikey> likes = this.<Likey, QLikey>createList("likes", Likey.class, QLikey.class, PathInits.DIRECT2);

    public final cos.peerna.domain.problem.model.QProblem problem;

    public final cos.peerna.domain.user.model.QUser user;

    public QReply(String variable) {
        this(Reply.class, forVariable(variable), INITS);
    }

    public QReply(Path<? extends Reply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReply(PathMetadata metadata, PathInits inits) {
        this(Reply.class, metadata, inits);
    }

    public QReply(Class<? extends Reply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.history = inits.isInitialized("history") ? new cos.peerna.domain.history.model.QHistory(forProperty("history"), inits.get("history")) : null;
        this.problem = inits.isInitialized("problem") ? new cos.peerna.domain.problem.model.QProblem(forProperty("problem")) : null;
        this.user = inits.isInitialized("user") ? new cos.peerna.domain.user.model.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

