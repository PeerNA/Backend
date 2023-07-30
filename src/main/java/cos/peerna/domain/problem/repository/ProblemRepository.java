package cos.peerna.domain.problem.repository;

import cos.peerna.domain.problem.model.ProblemIdMapping;
import cos.peerna.domain.problem.model.Problem;
import cos.peerna.domain.user.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findProblemByQuestion(String question);
    Long countByCategory(Category category);
    List<ProblemIdMapping> findAllByCategory(Category category);
}
