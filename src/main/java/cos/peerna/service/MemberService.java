package cos.peerna.service;

import cos.peerna.domain.Member;
import cos.peerna.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    public void join(Member member) {
        validateMember(member);
        memberRepository.save(member).getId();
    }

    private void validateMember(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new IllegalArgumentException("This email already exists.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
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
