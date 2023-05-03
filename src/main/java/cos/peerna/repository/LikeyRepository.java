package cos.peerna.repository;

import cos.peerna.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeyRepository extends JpaRepository<Likey, Long> {
    Optional<Likey> findLikeyByUserAndReply(User user, Reply reply);
}
