package cos.peerna.domain;

import cos.peerna.controller.dto.MemberRegisterRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "mbr_id")
    private Long id;

    private String name;
    private String email;
    private String password;
    private String profile;
    private String introduce;
    private float level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Embedded
    private Interest interests;

    public static Member createMember(MemberRegisterRequestDto dto) {
        Member member = new Member();
        member.name = dto.getName();
        member.email = dto.getEmail();
        member.password = dto.getPassword();
        member.profile = "";
        member.introduce = "";
        member.level = 0;
        member.interests = new Interest(Category.OS, Category.NETWORK, Category.DATA_STRUCTURE);
        member.role = Role.MENTEE;

        return member;
    }

    @Builder
    public Member(String name, String email, String profile, Role role) {
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.role = role;
    }

    public Member update(String name, String profile) {
        this.name = name;
        this.profile = profile;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
