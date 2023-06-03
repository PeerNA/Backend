package cos.peerna.service;

import cos.peerna.controller.dto.UserPatchRequestDto;
import cos.peerna.domain.*;
import cos.peerna.repository.FollowRepository;
import cos.peerna.repository.NotificationRepository;
import cos.peerna.repository.UserRepository;
import cos.peerna.security.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final NotificationRepository notificationRepository;
    private final HttpSession httpSession;

    // 회원 가입
    public void join(User user) {
        userRepository.save(user);
    }

    public void delete(SessionUser sessionUser) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        userRepository.delete(user);
    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public void patchUpdate(SessionUser sessionUser, UserPatchRequestDto dto) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        Interest interest = user.getInterests();
        Category priority1 = interest.getPriority1();
        Category priority2 = interest.getPriority2();
        Category priority3 = interest.getPriority3();
        Career career = user.getCareer();

        if (dto.getPriority1() != null) {
            priority1 = dto.getPriority1();
        }
        if (dto.getPriority2() != null) {
            priority2 = dto.getPriority2();
        }
        if (dto.getPriority3() != null) {
            priority3 = dto.getPriority3();
        }
        if (dto.getCareer() != null) {
            career = dto.getCareer();
        }

        Interest newInterest = new Interest(priority1, priority2, priority3);
        user.updateCondition(newInterest, career);
        sessionUser.updateInterest(newInterest);
        httpSession.setAttribute("user", sessionUser);
    }

    public void follow(Long followerId, Long followeeId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        User followee =
                userRepository.findById(followeeId).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        validateFollow(follower, followee);
        followRepository.save(new Follow(follower, followee));

        String msg = follower.getName() + "님이 회원님을 팔로우하기 시작했습니다.";
        NotificationType type = NotificationType.FOLLOW;
        if (followRepository.findByFollowerAndFollowee(followee, follower).isPresent()) {
            msg = follower.getName() + "님과 회원님이 서로를 팔로우하기 시작했습니다.";
            type = NotificationType.FOLLOW_EACH;
        }

        notificationRepository.save(Notification.builder()
                .user(followee)
                .msg(msg)
                .follower(follower)
                .type(type)
                .build());
    }

    public void unfollow(Long followerId, Long followeeId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        User followee =
                userRepository.findById(followeeId).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        followRepository.delete(followRepository.findByFollowerAndFollowee(follower, followee)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Already Not Followed")));
    }

    public void validateFollow(User follower, User followee) {
        if (followee.equals(follower)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Can't Follow Self");
        }
        if (followRepository.findByFollowerAndFollowee(follower, followee).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already Followed");
        }
    }
}
