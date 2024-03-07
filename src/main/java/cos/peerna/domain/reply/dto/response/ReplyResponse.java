package cos.peerna.domain.reply.dto.response;

import lombok.Builder;

@Builder
public record ReplyResponse(
        Long replyId,
        Long likes,
        String question,
        String answer,
        Long userId,
        String userName,
        String userImage
) {
    @Override
    public String toString() {
        return "ReplyResponse{" +
                "replyId=" + replyId
                + ", likes=" + likes
                + ", question='" + question + '\''
                + ", answer='" + answer + '\''
                + ", userId=" + userId
                + ", userName='" + userName + '\''
                + ", userImage='" + userImage + '\''
                + '}';
    }

    public static ReplyResponse of(Long replyId, Long likes, String question, String answer, Long userId, String userName, String userImage) {
        return new ReplyResponse(replyId, likes, question, answer, userId, userName, userImage);
    }
}
