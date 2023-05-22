package cos.peerna.controller.dto;

import cos.peerna.controller.dto.data.NotificationData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NotificationResponseDto {
	private List<NotificationData> notificationList;
}
