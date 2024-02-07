package cos.peerna.domain.user.model;

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
    @Column(nullable = false)
    private Role role;
    @Column
    private Category category;

    @OneToMany(mappedBy="followee", cascade=CascadeType.PERSIST)
    private final List<Follow> followers = new LinkedList<>();
    @OneToMany(mappedBy="follower", cascade=CascadeType.PERSIST)
    private final List<Follow> followings = new LinkedList<>();

    @Builder
    public User(Long id, String name, String email, String imageUrl, String introduce, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
        this.introduce = introduce;
        this.password = new BCryptPasswordEncoder().encode("password");
        this.score = 1000;
        this.category = Category.OPERATING_SYSTEM;
    }

    public User updateProfile(String name, String email, String profile, String introduce) {
        this.name = name;
        this.email = email;
        this.imageUrl = profile;
        this.introduce = introduce;

        return this;
    }

    public void updateScore(Integer point) {
        this.score += point;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
