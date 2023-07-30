package cos.peerna.domain.room.repository;

import cos.peerna.domain.room.model.ConnectedUser;
import org.springframework.data.repository.CrudRepository;

public interface ConnectedUserRepository extends CrudRepository<ConnectedUser, Long> {

}
