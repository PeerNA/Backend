package cos.peerna.controller.dto;

import cos.peerna.domain.History;
import cos.peerna.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplyResponseDto {
    private Long    replyId;
    private Long    userId;
    private String  name;
    private String  imageUrl;
    private String  answer;

}



