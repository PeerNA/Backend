package cos.peerna.controller.dto;

import cos.peerna.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
public class MatchedUserDto {
    private Long id;
    private String name;
    private String imageUrl;

    public MatchedUserDto (User peer) {
        this.id = peer.getId();
        this.name = peer.getName();
        this.imageUrl = peer.getImageUrl();
    }
}
