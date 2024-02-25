package cos.peerna.domain.match.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Standby {

    private final Long id;
    private final Integer score;
    private final LocalDateTime createdAt;

    @Builder
    public Standby(Long id, Integer score, LocalDateTime createdAt) {
        this.id = id;
        this.score = score;
        this.createdAt = createdAt;
    }

    public boolean isMatchable(Standby standby) {
        long waitingTime = ChronoUnit.SECONDS.between(this.createdAt, LocalDateTime.now());
        int scoreGap = Math.abs(this.score - standby.score);
        return scoreGap <= 50 + Math.log(1 + waitingTime) / Math.log(2);
    }
}
