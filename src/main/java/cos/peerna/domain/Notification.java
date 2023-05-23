package cos.peerna.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class Notification {

	@Id @GeneratedValue
	private Long id;

	private String msg;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reply_id", nullable = true)
	private Reply reply;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	private LocalDate time;

	public static Notification createNotification(User user, Reply reply, NotificationType type, String msg) {
		Notification notification = new Notification();
		notification.user = user;
		notification.reply = reply;
		notification.type = type;
		notification.msg = msg;
		notification.time = LocalDate.now();

		return notification;
	}

	public static void acceptNotification(Notification notification) {
		if (notification.type.equals(NotificationType.PULL_REQ)) {
			notification.type = NotificationType.PULL_REQ_ACC;
			notification.msg = "Pull-Request가 수락되었습니다.";
		}
		else if (notification.type.equals(NotificationType.FRIEND)) {
			notification.type = NotificationType.FRIEND_ACC;
			notification.msg = "친구 추가를 수락하였습니다.";
		}
		notification.time = LocalDate.now();
	}

	public static boolean isPRNotification(Notification notification) {
		return notification.type.equals(NotificationType.PULL_REQ_ACC);
	}
}
