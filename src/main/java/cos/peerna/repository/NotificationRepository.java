package cos.peerna.repository;

import cos.peerna.domain.Notification;
import cos.peerna.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByUser(User user);

	@Query("SELECT n FROM Notification n JOIN FETCH n.reply r JOIN FETCH r.problem p")
	Notification findNotificationById(Long id);
}
