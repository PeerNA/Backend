package cos.peerna.repository;

import cos.peerna.domain.Keyword;
import cos.peerna.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findKeywordsByProblem(Problem problem);
}
