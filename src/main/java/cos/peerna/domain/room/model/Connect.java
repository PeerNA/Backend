package cos.peerna.domain.room.model;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("Peerna:Connect")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Connect {
    @Id
    private Long id;
    private Integer roomId;

    public static Connect of(Long id, Integer roomId) {
        return new Connect(id, roomId);
    }
}
