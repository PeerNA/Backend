package cos.peerna.domain.reply.repository;

import cos.peerna.domain.history.model.History;
import cos.peerna.domain.problem.model.Problem;
import cos.peerna.domain.reply.model.Reply;
import cos.peerna.domain.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query("select r from Reply r join fetch r.user join fetch r.likes where r.id = :id")
    Optional<Reply> findByIdWithUserAndLike(Long id);
    @Query("select r from Reply r join fetch r.user join fetch r.history join fetch r.problem "
            + "where r.id > :cursorId and r.user.id = :userId order by r.id asc")
    List<Reply> findRepliesByUserIdOrderByIdAsc(Long userId, Long cursorId, Pageable pageable);
    @Query("select r from Reply r join fetch r.user where r.problem = :problem order by r.likeCount desc")
    List<Reply> findRepliesByProblemOrderByLikeCountDesc(Problem problem, Pageable pageable);
    Optional<Reply> findFirstByUserAndProblemOrderByIdDesc(User user, Problem problem);
    @Query("select r from Reply r join fetch r.user where r.history = :history order by r.id desc")
    List<Reply> findRepliesWithUserByHistoryOrderByHistoryIdDesc(History history);
}
