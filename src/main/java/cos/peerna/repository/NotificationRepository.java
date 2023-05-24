package cos.peerna.repository;

import cos.peerna.domain.Notification;
import cos.peerna.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("SELECT n FROM Notification n JOIN FETCH n.reply r JOIN FETCH r.problem p WHERE n.user = :user")
	List<Notification> findByUser(User user);

	List<Notification> findAllByUser(User user);

	@Query("SELECT n FROM Notification n JOIN FETCH n.reply r JOIN FETCH r.problem p WHERE n.id = :id")
	Notification findNotificationById(Long id);
}
