package cos.peerna.domain;

import cos.peerna.controller.dto.RoomResponseDto;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Data
@RedisHash("WaitingUser")
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
    private Long roomId;

    @Builder
    public WaitingUser(Long id, Category priority1, Category priority2, Category priority3, Career career) {
        this.id = id;
        this.priority1 = priority1;
        this.priority2 = priority2;
        this.priority3 = priority3;
        this.career = career;
        this.createdAt = LocalDateTime.now();
        this.roomId = -1L;
    }
}
