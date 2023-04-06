package cos.peerna.repository;

import cos.peerna.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeyRepository extends JpaRepository<Likey, Long> {
    Optional<Likey> findLikeyByUserAndUser(User user, Reply reply);
}
