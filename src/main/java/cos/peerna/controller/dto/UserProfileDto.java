package cos.peerna.controller.dto;

import cos.peerna.domain.User;
import lombok.Data;

@Data
public class UserProfileDto {
    private Long id;
    private String name;
    private String imageUrl;

    public UserProfileDto(User peer) {
        this.id = peer.getId();
        this.name = peer.getName();
        this.imageUrl = peer.getImageUrl();
    }
}
