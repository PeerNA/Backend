package cos.peerna.controller.dto.data;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class NotificationData {
	private Long        notificationId;
	private String      answer;
	private String      msg;
	private LocalDate   time;
}
