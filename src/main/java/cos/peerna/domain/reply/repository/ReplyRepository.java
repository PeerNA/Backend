package cos.peerna.domain.reply.repository;

import cos.peerna.domain.history.model.History;
import cos.peerna.domain.problem.model.Problem;
import cos.peerna.domain.reply.model.Reply;
import cos.peerna.domain.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findRepliesByUserOrderByIdDesc(User user, Pageable pageable);
    List<Reply> findRepliesByProblemOrderByLikeCountDesc(Problem problem, Pageable pageable);
    Optional<Reply> findFirstByUserAndProblemOrderByIdDesc(User user, Problem problem);
}
