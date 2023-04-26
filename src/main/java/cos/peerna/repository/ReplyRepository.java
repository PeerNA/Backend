package cos.peerna.repository;

import cos.peerna.domain.History;
import cos.peerna.domain.Problem;
import cos.peerna.domain.Reply;
import cos.peerna.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findRepliesByUserOrderByIdDesc(User user, Pageable pageable);
    List<Reply> findRepliesByProblem(Problem problem);
}
