package cos.peerna.domain.reply.dto.response;


public record GetReplyResponse(
        Long replyId,
        Long likes,
        String answer
) {
    @Override
    public String toString() {
        return "GetReplyResponse{" +
                "replyId=" + replyId
                + ", likes=" + likes
                + ", answer='" + answer + '\''
                + '}';
    }

    public static GetReplyWithProfileResponse from(Long replyId, Long likes,
                                                   String answer, Long userId,
                                                   String username, String profileImage) {
        return new GetReplyWithProfileResponse(replyId, likes, answer, userId, username, profileImage);
    }
}
