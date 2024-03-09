package cos.peerna.domain.room.service;

import cos.peerna.domain.history.model.History;
import cos.peerna.domain.history.repository.HistoryRepository;
import cos.peerna.domain.room.event.CreateRoomEvent;
import cos.peerna.domain.room.model.Room;
import cos.peerna.domain.room.repository.RoomRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final SimpMessagingTemplate template;
    private final RoomRepository roomRepository;
    private final HistoryRepository historyRepository;

    public void createRoom(CreateRoomEvent event) {
        List<Long> connectedUserIds = new ArrayList<>(event.userScore().keySet());
        event.userScore().entrySet().stream()
                .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> {
                    Long userId = entry.getKey();
                    connectedUserIds.add(userId);
                });

        History history = historyRepository.save(History.of(LocalDate.now()));
        Room room = roomRepository.save(Room.builder()
                        .category(event.category())
                        .historyId(history.getId())
                        .connectedUserIds(connectedUserIds)
                        .build());

        for (Long userId : connectedUserIds) {
            template.convertAndSend("/user/" + userId + "/match/join", room);
        }
    }
}
