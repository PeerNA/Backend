package cos.peerna.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * 동료매칭이 성사되어 연결되어 있는 유저
 * Redis 안에 저장되어 있다가 연결이 끊기면 삭제된다.
 * roomId 는 당장은 필요없지만, 다음과 같은 상황에서 필요함
 * userA 와 userB가 연결되어 있을 때,
 * userB가 Wi-fi를 끊고 다시 연결한다던가, 실수로 창을 다시 닫았다가 다시 접속할 때
 */
@Data
public class ConnectedUser {

    @Id
    private Long id;
    private boolean proceedAgree;
    private LocalDateTime lastConnectedAt;

    public ConnectedUser(Long id) {
        this.id = id;
        this.proceedAgree = false;
        this.lastConnectedAt = LocalDateTime.now();
    }
}
