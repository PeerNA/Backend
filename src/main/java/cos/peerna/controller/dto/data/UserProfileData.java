package cos.peerna.controller.dto.data;

import cos.peerna.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileData {
    private Long userId;
    private String name;
    private String imageUrl;
    private Integer score;

    public UserProfileData(User peer) {
        this.userId = peer.getId();
        this.name = peer.getName();
        this.imageUrl = peer.getImageUrl();
        this.score = peer.getScore();
    }
}
