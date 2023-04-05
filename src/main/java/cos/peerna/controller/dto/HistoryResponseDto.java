package cos.peerna.controller.dto;

import cos.peerna.domain.Category;
import cos.peerna.domain.History;
import cos.peerna.domain.Problem;
import cos.peerna.domain.Reply;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HistoryResponseDto {
    Long    historyId;
    Problem problem;
    List<ReplyResponseDto>  replyResponseDtoList;
}



