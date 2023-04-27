package cos.peerna.repository;

import cos.peerna.domain.Keyword;
import cos.peerna.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findKeywordsByProblem(Problem problem);

    List<Keyword> findKeywordsByProblemOrderByCountDesc(Problem problem);
    Optional<Keyword> findKeywordByNameAndProblem(String name, Problem problem);
}
