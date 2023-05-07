package cos.peerna.controller.dto.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplyData {
    private Long    replyId;
    private Long    userId;
    private Long    likes;
    private String  name;
    private String  imageUrl;
    private String  answer;

}



