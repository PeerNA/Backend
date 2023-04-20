package cos.peerna.repository;

import cos.peerna.domain.Category;
import cos.peerna.domain.History;
import cos.peerna.domain.Problem;
import cos.peerna.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findHistoriesById(Long id);
}
