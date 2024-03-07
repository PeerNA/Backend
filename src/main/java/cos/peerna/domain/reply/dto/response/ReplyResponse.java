package cos.peerna.domain.reply.dto.response;

import lombok.Builder;

@Builder
public record ReplyResponse(
        Long replyId,
        Long problemId,
        Long likes,
        String question,
        String answer,
        String exampleAnswer,
        Long userId,
        String userName,
        String userImage
) {
    public static ReplyResponse of(Long replyId, Long problemId, Long likes, String question, String answer, String exampleAnswer,
                                   Long userId, String userName, String userImage) {
        return new ReplyResponse(replyId, problemId, likes, question, answer, exampleAnswer, userId, userName, userImage);
    }

    @Override
    public String toString() {
        return "ReplyResponse{" +
                "replyId=" + replyId
                + ", problemId=" + problemId
                + ", likes=" + likes
                + ", question='" + question + '\''
                + ", answer='" + answer + '\''
                + ", exampleAnswer='" + exampleAnswer + '\''
                + ", userId=" + userId
                + ", userName='" + userName + '\''
                + ", userImage='" + userImage + '\''
                + '}';
    }
}
