package cos.peerna.domain;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Data
@RedisHash("Room")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private Category category;
    private LinkedList<Long> historyIdList = new LinkedList<>();
    private List<Long> connectedUserIdList = new ArrayList<>();

    @Builder
    public Room(List<Long> connectedUserIds, Long historyId, Category category) {
        this.connectedUserIdList = connectedUserIds;
        this.historyIdList.add(historyId);
        this.category = category;
    }
}
