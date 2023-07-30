package cos.peerna.domain.user.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInterest is a Querydsl query type for Interest
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QInterest extends BeanPath<Interest> {

    private static final long serialVersionUID = 1650657562L;

    public static final QInterest interest = new QInterest("interest");

    public final EnumPath<Category> priority1 = createEnum("priority1", Category.class);

    public final EnumPath<Category> priority2 = createEnum("priority2", Category.class);

    public final EnumPath<Category> priority3 = createEnum("priority3", Category.class);

    public QInterest(String variable) {
        super(Interest.class, forVariable(variable));
    }

    public QInterest(Path<? extends Interest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInterest(PathMetadata metadata) {
        super(Interest.class, metadata);
    }

}

