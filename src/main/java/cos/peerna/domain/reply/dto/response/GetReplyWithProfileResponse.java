package cos.peerna.domain.reply.dto.response;

public record GetReplyWithProfileResponse(
        Long replyId,
        Long likes,
        String answer,
        Long userId,
        String username,
        String profileImage
) {
    @Override
    public String toString() {
        return "GetReplyWithProfileResponse{" +
                "replyId=" + replyId
                + ", likes=" + likes
                + ", answer='" + answer + '\''
                + ", userId=" + userId
                + ", username='" + username + '\''
                + ", profileImage='" + profileImage + '\''
                + '}';
    }

    public static GetReplyWithProfileResponse from(Long replyId, Long likes,
                                                   String answer, Long userId,
                                                   String username, String profileImage) {
        return new GetReplyWithProfileResponse(replyId, likes, answer, userId, username, profileImage);
    }
}
