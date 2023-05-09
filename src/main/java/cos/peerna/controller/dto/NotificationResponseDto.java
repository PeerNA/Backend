package cos.peerna.controller.dto;

import cos.peerna.domain.Notification;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NotificationResponseDto {
	private List<Notification> notificationList;
}
