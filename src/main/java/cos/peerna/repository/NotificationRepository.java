package cos.peerna.repository;

import cos.peerna.domain.Notification;
import cos.peerna.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> 3findByUser(User user);
}
