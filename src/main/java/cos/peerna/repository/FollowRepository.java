package cos.peerna.repository;

import cos.peerna.domain.Follow;
import cos.peerna.domain.History;
import cos.peerna.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowee(User follower, User followee);
}
