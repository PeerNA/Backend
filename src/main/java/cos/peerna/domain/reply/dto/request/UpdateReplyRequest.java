package cos.peerna.domain.reply.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Validated
public record UpdateReplyRequest(
        @NotNull Long problemId,
        @NotBlank @Length(max=1000) String answer
) {
    @Override
    public String toString() {
        return "UpdateReplyRequest{" +
                "problemId=" + problemId +
                ", answer='" + answer + '\'' +
                '}';
    }
}