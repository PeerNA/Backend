package cos.peerna.controller;

import cos.peerna.controller.dto.DetailHistoryResponseDto;
import cos.peerna.controller.dto.RoomResponseDto;
import cos.peerna.domain.*;
import cos.peerna.repository.HistoryRepository;
import cos.peerna.repository.RoomRepository;
import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.service.HistoryService;
import cos.peerna.service.ImageService;
import cos.peerna.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.lang.module.ResolutionException;


@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomRepository roomRepository;
    private final HistoryService historyService;
    private final HistoryRepository historyRepository;
    private final ImageService imageService;


    @GetMapping("/api/match")
    public DeferredResult<ResponseEntity<RoomResponseDto>> match(@LoginUser SessionUser user,
                                                                 @RequestParam Integer player) {
        DeferredResult<ResponseEntity<RoomResponseDto>> deferredResult = new DeferredResult<>();

        if (user == null) {
            deferredResult.setResult(ResponseEntity.status(401).build());
            return deferredResult;
        }

        if (roomService.findAlreadyExist(user, deferredResult, player)) {
            return deferredResult;
        }

        if (player == 1) {
            roomService.soloMatch(user, deferredResult);
        } else {
            roomService.duoMatch(user, deferredResult);
        }
        return deferredResult;
    }



    @GetMapping("/api/match/next")
    public DeferredResult<ResponseEntity<RoomResponseDto>> next(@LoginUser SessionUser user,
                                                                @RequestParam Integer roomId,
                                                                @RequestParam Long peerId) {

        DeferredResult<ResponseEntity<RoomResponseDto>> deferredResult = new DeferredResult<>();
        if (user == null) {
            deferredResult.setResult(ResponseEntity.status(401).build());
            return deferredResult;
        }

        try {
            if (peerId == 0)
                roomService.soloNext(user, roomId, deferredResult);
            else {
                roomService.duoNext(user, roomId, peerId, deferredResult);
            }
        } catch (NullPointerException e) {
            log.error("NullPointerException: {}", e.getMessage());
            deferredResult.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        }


        return deferredResult;
    }

    // match 대기열 취소
    @DeleteMapping("/api/match")
    public ResponseEntity<String> matchCancel(@LoginUser SessionUser user) {
        if (user == null)
            return ResponseEntity.status(401).build();

        return roomService.matchCancel(user);
    }

    // 방 나가기 (모두 나가면 방 삭제)
    @DeleteMapping("/api/room")
    public ResponseEntity<String> leave(@LoginUser SessionUser user, @RequestParam Integer roomId) {
        if (user == null)
            return ResponseEntity.status(401).build();

        return roomService.leave(user, roomId);
    }

    @GetMapping("/api/match/status")
    public DeferredResult<ResponseEntity<DetailHistoryResponseDto>> next(@LoginUser SessionUser user,
                                                                         @RequestParam Integer roomId) {

        DeferredResult<ResponseEntity<DetailHistoryResponseDto>> deferredResult = new DeferredResult<>();
        if (user == null) {
            deferredResult.setResult(ResponseEntity.status(401).build());
            return deferredResult;
        }
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            deferredResult.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
            return deferredResult;
        }

        History history = historyRepository.findById(room.getHistoryIdList().getLast())
                .orElseThrow(() -> new ResolutionException("History not found"));
        if (history.isSolved()) {
            deferredResult.setResult(ResponseEntity.ok(historyService.findDetailHistory(user, history.getId())));
        } else {
            deferredResult.setResult(ResponseEntity.accepted().build());
        }

        return deferredResult;
    }

    @PostMapping("/api/match/upload")
    public ResponseEntity<String> uploadImage(@LoginUser SessionUser user,
                                              @RequestParam MultipartFile file) throws IOException {
        long fileSize = file.getSize();
        if (fileSize > 1024 * 1024 * 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File size is too big");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File type is not supported");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                imageService.uploadImage(user.getId(), file)
        );
    }
}
