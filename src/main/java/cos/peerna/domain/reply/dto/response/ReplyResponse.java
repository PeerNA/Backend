package cos.peerna.domain.reply.dto.response;

import lombok.Builder;

@Builder
public record ReplyResponse(
        Long replyId,
        Long likes,
        String answer,
        Long userId,
        String userName,
        String userImage
) {
    @Override
    public String toString() {
        return "GetReplyWithProfileResponse{" +
                "replyId=" + replyId
                + ", likes=" + likes
                + ", answer='" + answer + '\''
                + ", userId=" + userId
                + ", userName='" + userName + '\''
                + ", userImage='" + userImage + '\''
                + '}';
    }
}
