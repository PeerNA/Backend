package cos.peerna.controller.dto;

import cos.peerna.controller.dto.data.UserProfileData;
import cos.peerna.domain.Problem;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomResponseDto {
    private Integer roomId;
    private Long    historyId;
    private Problem problem;
    private UserProfileData peer;

}
