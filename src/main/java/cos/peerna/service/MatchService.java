package cos.peerna.service;

import cos.peerna.domain.Category;
import cos.peerna.domain.Room;
import cos.peerna.domain.WaitingUser;
import cos.peerna.repository.UserRepository;
import cos.peerna.repository.WaitingUserRepository;
import cos.peerna.security.dto.SessionUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final WaitingUserRepository waitingUserRepository;
    private final UserRepository userRepository;

    @Transactional
    public void match(SessionUser user, DeferredResult<ResponseEntity<Room>> deferredResult) {
//        List<WaitingUser> all = (List<WaitingUser>) waitingUserRepository.findAll();
//        all.stream().map(waitingUser -> waitingUser.getPriority1()).forEach(p1 -> {
//            log.debug("is_same: {}", p1.compareTo(user.getInterest().getPriority1()));
//        });

        log.debug("findAll():{}", waitingUserRepository.findAll());
        Category selectedCategory = user.getInterest().getPriority1();
        log.debug("selectedCategory: {}", selectedCategory);
        List<WaitingUser> matchedUsers =
                waitingUserRepository.findByPriority1(selectedCategory);
        if (matchedUsers.size() == 0) {
            selectedCategory = user.getInterest().getPriority2();
            matchedUsers = waitingUserRepository.findByPriority2OrderByCreatedAt(selectedCategory);
            if (matchedUsers.size() == 0) {
                selectedCategory = user.getInterest().getPriority3();
                matchedUsers = waitingUserRepository.findByPriority3OrderByCreatedAt(selectedCategory);
            }
        }
        log.info("matchedUsers: {}", matchedUsers);
        if (matchedUsers.size() == 0) {
            log.debug("{}: No matched user. Waiting for another user to join.", user.getName());
            waitingUserRepository.save(WaitingUser.builder()
                    .priority1(user.getInterest().getPriority1())
                    .priority2(user.getInterest().getPriority2())
                    .priority3(user.getInterest().getPriority3())
                    .career(user.getCareer())
                    .id(user.getId())
                    .build());
        } else {
            log.debug("{}: Matched with {}", user.getName(), matchedUsers.get(0).getId());
            WaitingUser waitingUser = matchedUsers.get(0);
            waitingUserRepository.delete(waitingUser);
            ResponseEntity<Room> okResponse = ResponseEntity.ok(Room.builder()
                    .user1(new SessionUser(userRepository.findById(waitingUser.getId()).get()))
                    .user2(user)
                    .category(selectedCategory)
                    .build());
            deferredResult.setResult(okResponse);
        }
    }
}
