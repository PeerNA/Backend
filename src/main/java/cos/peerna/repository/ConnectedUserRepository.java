package cos.peerna.repository;

import cos.peerna.domain.Category;
import cos.peerna.domain.ConnectedUser;
import cos.peerna.domain.WaitingUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConnectedUserRepository extends CrudRepository<ConnectedUser, Long> {

}
