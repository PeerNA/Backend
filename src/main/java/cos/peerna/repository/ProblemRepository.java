package cos.peerna.repository;

import cos.peerna.domain.Category;
import cos.peerna.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findById(Long id);
    Optional<Problem> findProblemByQuestion(String question);
    List<Problem> findProblemsByCategory(Category question);
}
