package cos.peerna.repository;

import cos.peerna.domain.History;
import cos.peerna.domain.Problem;
import cos.peerna.domain.Reply;
import cos.peerna.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findRepliesByHistory(History history);
    List<Reply> findRepliesByUserOrderByIdDesc(User user, Pageable pageable);
    List<Reply> findRepliesByProblem(Problem problem);

    List<Reply> findRepliesByProblemOrderByLikeCountDesc(Problem problem, Pageable pageable);

    Long countByProblem(Problem problem);

//    @Query("SELECT r FROM Reply r LEFT JOIN FETCH r.likes WHERE r.problem = :problem ORDER BY SIZE(r.likes) DESC")
//    List<Reply> findRepliesByProblemOrderByLikeCountDesc(@Param("problem") Problem problem);
}
