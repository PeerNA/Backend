package cos.peerna.domain.github.event;

import cos.peerna.domain.problem.model.Problem;

public record CommitReplyEvent(
        String githubLogin,
        String githubToken,
        Problem problem,
        String answer
) {
    @Override
    public String toString() {
        return "CommitReplyEvent{" +
                "githubLogin='" + githubLogin + '\'' +
                ", githubToken='" + githubToken + '\'' +
                ", problem=" + problem +
                ", answer='" + answer + '\'' +
                '}';
    }

    public static CommitReplyEvent of(String githubLogin, String githubToken, Problem problem, String answer) {
        return new CommitReplyEvent(githubLogin, githubToken, problem, answer);
    }
}
