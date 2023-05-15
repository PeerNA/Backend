package cos.peerna.service;

import cos.peerna.controller.dto.MatchedUserDto;
import cos.peerna.controller.dto.RoomResponseDto;
import cos.peerna.domain.*;
import cos.peerna.repository.*;
import cos.peerna.security.dto.SessionUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final WaitingUserRepository waitingUserRepository;
    private final ConnectedUserRepository connectedUserRepository;
    private final RoomRepository roomRepository;
    private final HistoryRepository historyRepository;
    private final HistoryService historyService;
    private final ProblemService problemService;
    private final UserRepository userRepository;

    public void soloMatch(SessionUser user, DeferredResult<ResponseEntity<RoomResponseDto>> deferredResult) {
        Problem problem = problemService.getRandomByCategory(user.getInterest().getPriority1()).orElse(null);
        History history = historyRepository.save(History.createHistory(problem));

        Room room = roomRepository.save(Room.builder()
                .connectedUserIds(new ArrayList<>(List.of(user.getId())))
                .historyId(history.getId())
                .category(user.getInterest().getPriority1())
                .build());
        connectedUserRepository.save(new ConnectedUser(user.getId(), room.getId()));
        deferredResult.setResult(
                ResponseEntity.ok(
                        RoomResponseDto.builder()
                                .roomId(room.getId())
                                .historyId(history.getId())
                                .problem(history.getProblem())
                                .peer(null)
                                .build()));
    }
    @Transactional
    public void duoMatch(SessionUser user, DeferredResult<ResponseEntity<RoomResponseDto>> deferredResult) {
        /*
         대기열에서 자신을 찾고 존재한다면 이미 매칭이 됐는지 확인
         이미 매칭 O -> 대기열에서 자신을 삭제하고 방 정보를 반환
         이미 매칭 X -> 매칭 시도 -> 대기열에서 자신과 비슷한 유저를 찾을 때, 후보들에 자신이 있으면 안 되니 잠시 삭제
         */
        WaitingUser findSelf = waitingUserRepository.findById(user.getId()).orElse(null);
        if (findSelf != null) {
            if (checkMatched(user, deferredResult, findSelf)) return;
        }

        // 현재 유저와 같은 관심사를 가진 유저를 찾아 리스트로 반환, 학습 경력은 무시
        Category selectedCategory = user.getInterest().getPriority1();
        List<WaitingUser> matchedUsers = matchByUser(user, selectedCategory);

        /*
         매칭된 유저가 없다면 현재 유저를 Repository 에 저장하고 202(Accepted) 응답 반환
         Accepted = 요청이 접수되었으며, 작업이 완료되지 않았음 -> 비동기 처리를 위해 사용
         202 응답을 받은 클라이언트는 일정 시간 후 다시 매칭 요청을 보냄
         */
        if (matchedUsers.size() == 0) {
            log.debug("{}: No matched user. Waiting for another user to join.", user.getName());
            if (findSelf == null) {
                findSelf = WaitingUser.builder()
                        .priority1(user.getInterest().getPriority1())
                        .priority2(user.getInterest().getPriority2())
                        .priority3(user.getInterest().getPriority3())
                        .career(user.getCareer())
                        .id(user.getId())
                        .build();
            }
            waitingUserRepository.save(findSelf);
            deferredResult.setResult(ResponseEntity.accepted().build());
        } else {
            // 매칭된 유저가 있다면 매칭된 유저와 방을 생성하고 200(OK) 응답 반환
            WaitingUser matchedUser = matchedUsers.get(0);
            log.debug("{}: Matched with {}", user.getName(), matchedUser.getId());

            Problem problem = problemService.getRandomByCategory(selectedCategory).orElse(null);
            History history = historyRepository.save(History.createHistory(problem));
            User peer = userRepository.findById(matchedUser.getId()).orElse(null);

            Room room = roomRepository.save(Room.builder()
                    .connectedUserIds(new ArrayList<>(List.of(user.getId(), matchedUser.getId())))
                    .historyId(history.getId())
                    .category(selectedCategory)
                    .build());
            connectedUserRepository.save(new ConnectedUser(user.getId(), room.getId()));
            log.debug("Second User's RoomId: {}", room.getId());
            matchedUser.setRoomId(room.getId());
            waitingUserRepository.save(matchedUser);
            deferredResult.setResult(
                    ResponseEntity.ok(
                            RoomResponseDto.builder()
                                    .roomId(room.getId())
                                    .historyId(history.getId())
                                    .problem(history.getProblem())
                                    .peer(new MatchedUserDto(peer))
                                    .build()));
        }
    }

    private boolean checkMatched(SessionUser user, DeferredResult<ResponseEntity<RoomResponseDto>> deferredResult, WaitingUser findSelf) {
        log.debug("findSelf:{} rooId: {}", findSelf.getId(), findSelf.getRoomId());
        waitingUserRepository.delete(findSelf);
        if (findSelf.getRoomId() != -1L) {
            log.debug("{}: Already Matched", user.getName());

            Room room = roomRepository.findById(findSelf.getRoomId()).orElse(null);
            connectedUserRepository.save(new ConnectedUser(user.getId(), room.getId()));

            User peer = userRepository.findById(room.getConnectedUserIdList().get(0)).orElse(null);
            History history = historyRepository.findById(room.getHistoryIdList().get(0)).orElse(null);
            log.debug("Loading problem: {}", history.getProblem().getAnswer());
            log.debug("First User's RoomId: {}", room.getId());
            deferredResult.setResult(
                    ResponseEntity.ok(
                            RoomResponseDto.builder()
                                    .roomId(room.getId())
                                    .historyId(history.getId())
                                    .problem(history.getProblem())
                                    .peer(new MatchedUserDto(peer))
                                    .build()));
            return true;
        }
        return false;
    }

    private List<WaitingUser> matchByUser(SessionUser user, Category selectedCategory) {
        List<WaitingUser> matchedUsers = waitingUserRepository.findByPriority1OrderByCreatedAt(selectedCategory);
        if (matchedUsers.size() == 0) {
            selectedCategory = user.getInterest().getPriority2();
            matchedUsers = waitingUserRepository.findByPriority2OrderByCreatedAt(selectedCategory);
            if (matchedUsers.size() == 0) {
                selectedCategory = user.getInterest().getPriority3();
                matchedUsers = waitingUserRepository.findByPriority3OrderByCreatedAt(selectedCategory);
            }
        }
        return matchedUsers;
    }

    @Transactional
    public void duoNext(SessionUser user, Integer roomId, Long peerId,
                        DeferredResult<ResponseEntity<RoomResponseDto>> deferredResult) throws NullPointerException {
        Room room = roomRepository.findById(roomId).orElse(null);
        ConnectedUser self = connectedUserRepository.findById(user.getId()).orElse(null);
        self.setProceedAgree(true);
        self.setLastConnectedAt(LocalDateTime.now());
        connectedUserRepository.save(self);
        roomRepository.save(room);
        ConnectedUser peer = connectedUserRepository.findById(peerId).orElse(null);
        for (Long id : room.getHistoryIdList()) {
            log.debug("historyId: {}", id);
        }
        log.debug("getFirst(): {}", room.getHistoryIdList().getFirst());
        log.debug("getLast(): {}", room.getHistoryIdList().getLast());
        History history = historyRepository.findById(room.getHistoryIdList().getLast()).orElse(null);
        if (!peer.isProceedAgree() && history.isSolved()) { // peer 가 아직 동의하지 않았고, 새로운 history가 만들어져있지 않은 상태
            /*
              TODO: peer 가 10초 이상 응답이 없으면 연결을 끊어야 함. 중간에 동료가 상의없이 나갈 경우를 대비
              if (peer.getLastConnectedAt().isBefore(LocalDateTime.now().minusSeconds(10))) {
                              log.debug("{}: peer:{} is not responding", user.getName(), peer.getId());
                              deferredResult.setResult(ResponseEntity.status(HttpStatus.GONE).build());
                              return;
                          }
             */

            log.debug("{}: Waiting for peer({})'s agreement", user.getName(), peer.getId());
            deferredResult.setResult(ResponseEntity.accepted().build());
            return;
        }


        if (history.isSolved()) { // 마지막 history가 풀린 상태, 즉 새로운 history가 필요한 상태
            Problem problem = problemService.getRandomByCategoryNonDuplicate(room.getCategory(), room.getHistoryIdList());
            history = historyService.createHistory(problem.getId(), room.getId());
        } else { // 마지막 history가 풀리지 않은 상태, 즉 이미 동료가 만들어둔 상태라면 그대로 사용 + 진행동의 초기화
            peer.setProceedAgree(false);
            self.setProceedAgree(false);
            connectedUserRepository.save(peer);
            connectedUserRepository.save(self);
            roomRepository.save(room);
        }
        log.debug("Loading problem: {}", history.getProblem().getAnswer());
        deferredResult.setResult(
                ResponseEntity.ok(
                        RoomResponseDto.builder()
                                .roomId(room.getId())
                                .historyId(history.getId())
                                .problem(history.getProblem())
                                .build()));
    }
    public void soloNext (SessionUser user, Integer roomId,
                          DeferredResult < ResponseEntity < RoomResponseDto >> deferredResult) {
        Room room = roomRepository.findById(roomId).orElse(null);
        Problem problem = problemService.getRandomByCategoryNonDuplicate(room.getCategory(), room.getHistoryIdList());
        History history = historyService.createHistory(problem.getId(), room.getId());
        deferredResult.setResult(
                ResponseEntity.ok(
                        RoomResponseDto.builder()
                                .roomId(room.getId())
                                .historyId(history.getId())
                                .problem(history.getProblem())
                                .build()));

    }

    public ResponseEntity<String> matchCancel(SessionUser user) {
        waitingUserRepository.deleteById(user.getId());
        return ResponseEntity.ok().body("success");
    }

    public ResponseEntity<String> leave(SessionUser user, Integer roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return ResponseEntity.badRequest().body("Room not found");
        }
        for (Long id : room.getConnectedUserIdList()) {
            if (id.equals(user.getId())) {
                room.getConnectedUserIdList().remove(id);
                connectedUserRepository.deleteById(id);
                if (room.getConnectedUserIdList().size() == 0) {
                    roomRepository.deleteById(roomId);
                } else {
                    roomRepository.save(room);
                }
                return ResponseEntity.ok().body("success");
            }
        }
        return ResponseEntity.badRequest().body("User not found");
    }


}
