package cos.peerna.service;

import cos.peerna.security.dto.SessionUser;
import cos.peerna.domain.Career;
import cos.peerna.domain.Interest;
import cos.peerna.domain.User;
import cos.peerna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 가입
    public void join(User user) {
        userRepository.save(user);
    }

    public void delete(SessionUser sessionUser) {
        User user = userRepository.findById(sessionUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        userRepository.delete(user);
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
        User user = userRepository.findById(sessionUser.getId()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.updateCondition(interest, career);
    }
}
