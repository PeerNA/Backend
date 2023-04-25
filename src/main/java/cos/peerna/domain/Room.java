package cos.peerna.domain;

import cos.peerna.security.dto.SessionUser;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Room")
public class Room {
    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private SessionUser user1;
    private SessionUser user2;
    private Category category;

    @Builder
    public Room(SessionUser user1, SessionUser user2, Category category) {
        this.user1 = user1;
        this.user2 = user2;
        this.category = category;
    }
}
