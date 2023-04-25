package cos.peerna.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Data
@RedisHash("WaitingUser")
public class WaitingUser {

    @Id
    private Long id;
    private Category priority1;
    private Category priority2;
    private Category priority3;
    private Career career;
    private LocalDateTime createdAt;

    @Builder
    public WaitingUser(Long id, Category priority1, Category priority2, Category priority3, Career career) {
        this.id = id;
        this.priority1 = priority1;
        this.priority2 = priority2;
        this.priority3 = priority3;
        this.career = career;
        this.createdAt = LocalDateTime.now();
    }
}
