package cos.peerna.service;

import cos.peerna.config.auth.dto.SessionUser;
import cos.peerna.domain.Career;
import cos.peerna.domain.Interest;
import cos.peerna.domain.User;
import cos.peerna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 가입
    public void join(User user) {
        validateUser(user);
        userRepository.save(user);
    }

    private void validateUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("This email already exists.");
        }
    }

    // 회원 전체 조회
    public List<User> findUsers() {
        return userRepository.findAll();
    }

//    public Member findOne(Long memberId) {
//        return memberRepository.findById(memberId);
//    }

    @Transactional
    public void  updateCondition(SessionUser sessionUser, Interest interest, Career career) {
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.updateCondition(interest, career);
    }
}
