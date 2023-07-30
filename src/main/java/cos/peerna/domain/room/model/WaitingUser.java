package cos.peerna.domain.room.model;

import cos.peerna.domain.user.model.Career;
import cos.peerna.domain.user.model.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * 동료매칭을 기다리는 유저
 * 매칭을 기다리는 동안 Redis 안에 저장되어 있다가
 * 매칭이 성사되면 Redis에서 삭제된다.
 */
@Data
@RedisHash("WaitingUser")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingUser {

    @Id
    private Long id;
    @Indexed
    @Enumerated(EnumType.STRING)
    private Category priority1;
    @Indexed
    @Enumerated(EnumType.STRING)
    private Category priority2;
    @Indexed
    @Enumerated(EnumType.STRING)
    private Category priority3;
    @Indexed
    @Enumerated(EnumType.STRING)
    private Career career;
    private LocalDateTime createdAt;
    private Integer roomId;

    @Builder
    public WaitingUser(Long id, Category priority1, Category priority2, Category priority3, Career career) {
        this.id = id;
        this.priority1 = priority1;
        this.priority2 = priority2;
        this.priority3 = priority3;
        this.career = career;
        this.createdAt = LocalDateTime.now();
        this.roomId = -1;
    }
}
