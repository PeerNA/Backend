package cos.peerna.controller.dto;

import cos.peerna.controller.dto.data.ReplyData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReplyResponseDto {
	private List<ReplyData> replyData;
	private Long totalCount;
}
