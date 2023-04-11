package cos.peerna.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLikey is a Querydsl query type for Likey
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikey extends EntityPathBase<Likey> {

    private static final long serialVersionUID = -1832057398L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLikey likey = new QLikey("likey");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReply reply;

    public final QUser user;

    public QLikey(String variable) {
        this(Likey.class, forVariable(variable), INITS);
    }

    public QLikey(Path<? extends Likey> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLikey(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLikey(PathMetadata metadata, PathInits inits) {
        this(Likey.class, metadata, inits);
    }

    public QLikey(Class<? extends Likey> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reply = inits.isInitialized("reply") ? new QReply(forProperty("reply"), inits.get("reply")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

