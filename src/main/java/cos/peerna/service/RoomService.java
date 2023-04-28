package cos.peerna.service;

import cos.peerna.controller.dto.ProblemResponseDto;
import cos.peerna.controller.dto.RoomResponseDto;
import cos.peerna.domain.*;
import cos.peerna.repository.HistoryRepository;
import cos.peerna.repository.RoomRepository;
import cos.peerna.repository.UserRepository;
import cos.peerna.repository.WaitingUserRepository;
import cos.peerna.security.dto.SessionUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final WaitingUserRepository waitingUserRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final HistoryRepository historyRepository;
    private final ProblemService problemService;

    @Transactional
    public void match(SessionUser user, DeferredResult<ResponseEntity<RoomResponseDto>> deferredResult) {
        // 이미 매칭된 유저인지 확인
        // 매칭된 유저라면 대기열에서 자신을 삭제하고 방 정보를 반환
        // 매칭이 되지 않았더라도, 자신이랑 비슷한 유저를 찾을 때, 후보들에 자신이 있으면 안 되니 잠시 삭제
        WaitingUser findSelf = waitingUserRepository.findById(user.getId()).orElse(null);
        if (findSelf != null) {
            log.debug("findSelf:{} rooId: {}", findSelf.getId(), findSelf.getRoomId());
            waitingUserRepository.delete(findSelf);
            if (findSelf.getRoomId() != -1L) {
                log.debug("{}: Already Matched", user.getName());
                Room room = roomRepository.findById(findSelf.getRoomId()).orElse(null);
                History history = historyRepository.findHistoryByRoom(room).orElse(null);
                log.debug("Loading problem: {}", history.getProblem().getAnswer());
                deferredResult.setResult(
                        ResponseEntity.ok(
                                RoomResponseDto.builder()
                                        .roomId(room.getId())
                                        .historyId(history.getId())
                                        .problem(history.getProblem())
                                        .build()));
                return;
            }
        }

        // 현재 유저와 같은 관심사를 가진 유저를 찾아 리스트로 반환, 학습 경력은 무시
        Category selectedCategory = user.getInterest().getPriority1();
        List<WaitingUser> matchedUsers = matchByUser(user, selectedCategory);

        // 매칭된 유저가 없다면 현재 유저를 Repository 에 저장하고 202(Accepted) 응답 반환
        // Accepted = 요청이 접수되었으며, 작업이 완료되지 않았음 -> 비동기 처리를 위해 사용
        // 202 응답을 받은 클라이언트는 일정 시간 후 다시 매칭 요청을 보냄
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
            log.debug("{}: Matched with {}", user.getName(), matchedUsers.get(0).getId());
            WaitingUser matchedUser = matchedUsers.get(0);
            Room room = roomRepository.save(Room.builder()
                    .user1(userRepository.findById(matchedUser.getId()).get())
                    .user2(userRepository.findById(user.getId()).get())
                    .category(selectedCategory)
                    .build());
            matchedUser.setRoomId(room.getId());
            waitingUserRepository.save(matchedUser);
            Problem problem = problemService.getRandomByCategory(selectedCategory).orElse(null);
            History history = historyRepository.save(History.createHistory(problem, room));
            deferredResult.setResult(
                    ResponseEntity.ok(
                            RoomResponseDto.builder()
                                    .roomId(room.getId())
                                    .historyId(history.getId())
                                    .problem(history.getProblem())
                                    .build()));
        }
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
}
