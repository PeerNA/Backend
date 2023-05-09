package cos.peerna.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
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
}
