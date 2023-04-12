package cos.peerna.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatRoomDTO {

    private String roomId;
    private String name;

    /**
     * private Set<WebSocketSession> sessions = new HashSet<>();
     * WebSocketSession은 Spring에서 Websocket Connection이 맺어진 세션
     * stomp 를 쓰는 순간 위 세션도 알아서 관리해준다.
     */

    public static ChatRoomDTO create(String name){
        ChatRoomDTO room = new ChatRoomDTO();

        room.roomId = UUID.randomUUID().toString();
        room.name = name;
        return room;
    }
}