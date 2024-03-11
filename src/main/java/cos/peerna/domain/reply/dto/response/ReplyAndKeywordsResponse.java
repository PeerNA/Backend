package cos.peerna.domain.reply.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record ReplyAndKeywordsResponse(
        ReplyResponse replyResponse,
        List<String> keywords
) {
    public static ReplyAndKeywordsResponse of(ReplyResponse replyResponse, List<String> keywords) {
        return new ReplyAndKeywordsResponse(replyResponse, keywords);
    }

    @Override
    public String toString() {
        return "ReplyAndKeywordsResponse{" +
                "replyResponse=" + replyResponse
                + ", keywords=" + keywords
                + '}';
    }
}

