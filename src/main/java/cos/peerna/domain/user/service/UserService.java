package cos.peerna.domain.user.service;

import cos.peerna.domain.notification.model.Notification;
import cos.peerna.domain.notification.model.NotificationType;
import cos.peerna.domain.notification.repository.NotificationRepository;
import cos.peerna.domain.user.dto.UserPatchRequestDto;
import cos.peerna.domain.user.model.*;
import cos.peerna.domain.user.repository.FollowRepository;
import cos.peerna.domain.user.repository.UserRepository;
import cos.peerna.global.security.dto.SessionUser;
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

    public void checkForbiddenUser(User user, Long userId) {
        if (isDifferentUser(user, userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden User");
        }
    }

    private static boolean isDifferentUser(User user, Long userId) {
        return !user.getId().equals(userId);
    }

}
