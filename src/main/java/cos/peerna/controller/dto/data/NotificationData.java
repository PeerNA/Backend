package cos.peerna.controller.dto.data;

import cos.peerna.controller.dto.UserProfileDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class NotificationData {
	private Long        notificationId;
	private String		type;
	private String      answer;
	private UserProfileDto sender;
	private String      msg;
	private LocalDate   time;
}
