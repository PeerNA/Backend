package cos.peerna.repository;

import cos.peerna.domain.Category;
import cos.peerna.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findProblemByQuestion(String question);
    Long countByCategory(Category category);
}
