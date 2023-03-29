package cos.peerna.service;

import cos.peerna.domain.User;
import cos.peerna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

//    @Transactional
//    public void  update(Long id, String name) {
//    Optional -> Member 변환?
//        Member member = (Member) memberRepository.findById(id);
//        member.setName(name);
//    }
}
