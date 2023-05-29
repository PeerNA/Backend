package cos.peerna.controller.dto.data;

import cos.peerna.domain.User;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReplyData extends UserProfileData{
    private Long    replyId;
    private Long    likes;
    private String  answer;

    @Builder
    public ReplyData(User user, Long replyId, Long likes, String answer) {
        super(user);
        this.replyId = replyId;
        this.likes = likes;
        this.answer = answer;
    }
}



