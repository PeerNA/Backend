package cos.peerna.controller.dto;

import cos.peerna.domain.Category;
import cos.peerna.domain.Keyword;
import cos.peerna.domain.Problem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomResponseDto {
    private Long    roomId;
    private Long    historyId;
    private ProblemResponseDto problem;
}
