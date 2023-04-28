package cos.peerna.domain;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomUser> roomUsers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder
    public Room(User user1, User user2, Category category) {
        this.roomUsers.add(RoomUser.builder().room(this).user(user1).build());
        this.roomUsers.add(RoomUser.builder().room(this).user(user2).build());
        this.category = category;
    }
}
