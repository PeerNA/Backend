package cos.peerna.domain.user.model;

import cos.peerna.domain.user.dto.UserRegisterRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String email;
    private String password;
    @Column(name = "image_url")
    private String imageUrl;
    private String introduce;
    private Integer score;

    @Enumerated(EnumType.STRING)
    private Career career;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Embedded
    private Interest interests;

    @OneToMany(mappedBy="followee", cascade=CascadeType.PERSIST)
    private final List<Follow> followers = new LinkedList<>();
    @OneToMany(mappedBy="follower", cascade=CascadeType.PERSIST)
    private final List<Follow> followings = new LinkedList<>();

    public static User createUser(UserRegisterRequestDto dto) {
        User user = new User();
        user.id = dto.getId();
        user.name = dto.getName();
        user.email = dto.getEmail();
        user.password = dto.getPassword();
        user.imageUrl = "https://avatars.githubusercontent.com/u/0?v=4";
        user.introduce = "";
        user.career = Career.UNDER_1;
        user.interests = new Interest(Category.OS, Category.NETWORK, Category.DATA_STRUCTURE);
        user.role = Role.MENTEE;
        user.score = 1000;

        return user;
    }

    @Builder
    public User(Long id, String name, String email, String imageUrl, String bio, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
        this.introduce = bio;
        this.career = Career.UNDER_1;
        this.interests = new Interest(Category.OS, Category.NETWORK, Category.DATA_STRUCTURE);
        this.password = new BCryptPasswordEncoder().encode("password");
        this.score = 1000;
    }

    public User updateProfile(String name, String email, String profile, String introduce) {
        this.name = name;
        this.email = email;
        this.imageUrl = profile;
        this.introduce = introduce;

        return this;
    }

    public User updateCondition(Interest interests, Career career) {
        this.interests = interests;
        this.career = career;

        return this;
    }

    public void addScore(Integer point) {
        this.score += point;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
