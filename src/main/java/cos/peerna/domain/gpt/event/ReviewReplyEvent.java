package cos.peerna.domain.gpt.event;

public record ReviewReplyEvent(
        Long userId,
        String question,
        String answer
) {
    @Override
    public String toString() {
        return "ReviewReplyEvent{" +
                "userId=" + userId +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

    public static ReviewReplyEvent of(Long userId, String question, String answer) {
        return new ReviewReplyEvent(userId, question, answer);
    }
}
