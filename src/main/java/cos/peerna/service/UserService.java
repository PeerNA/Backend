package cos.peerna.service;

import cos.peerna.controller.dto.UserPatchRequestDto;
import cos.peerna.domain.Category;
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

import java.util.ArrayList;
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

    public void delete(SessionUser sessionUser) {
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
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
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.updateCondition(interest, career);
    }

    public void patchUpdate(SessionUser sessionUser, UserPatchRequestDto dto) {
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        Interest interest = user.getInterests();
        Category priority1 = interest.getPriority1();
        Category priority2 = interest.getPriority2();
        Category priority3 = interest.getPriority3();
        Career career = user.getCareer();

        List<UserPatchRequestDto> list = new ArrayList<>();
        list.add(dto);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPriority1() != null) {
                priority1 = list.get(i).getPriority1();
            }
            if (list.get(i).getPriority2() != null) {
                priority2 = list.get(i).getPriority2();
            }
            if (list.get(i).getPriority3() != null) {
                priority3 = list.get(i).getPriority3();
            }
            if (list.get(i).getCareer() != null) {
                career = list.get(i).getCareer();
            }
        }

        Interest newInterest = new Interest(priority1, priority2, priority3);
        user.updateCondition(newInterest, career);
    }
}
