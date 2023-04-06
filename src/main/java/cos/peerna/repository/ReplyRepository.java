package cos.peerna.repository;

import cos.peerna.domain.History;
import cos.peerna.domain.Problem;
import cos.peerna.domain.Reply;
import cos.peerna.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findRepliesByUser(User user);
    List<Reply> findRepliesByProblem(Problem problem);
}
