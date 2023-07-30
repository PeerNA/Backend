package cos.peerna.domain.room.repository;

import cos.peerna.domain.user.model.Category;
import cos.peerna.domain.room.model.WaitingUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WaitingUserRepository extends CrudRepository<WaitingUser, Long> {

    List<WaitingUser> findByPriority1OrderByCreatedAt(Category priority1);
    List<WaitingUser> findByPriority2OrderByCreatedAt(Category priority2);
    List<WaitingUser> findByPriority3OrderByCreatedAt(Category priority2);

}
