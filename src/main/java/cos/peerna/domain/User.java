package cos.peerna.domain;

import cos.peerna.controller.dto.UserRegisterRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name = "id")
    private Long id;

    private String name;
    private String email;
    private String password;
    @Column(name = "image_url")
    private String imageUrl;
    private String introduce;
    private int level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Embedded
    private Interest interests;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Like like;

    public static User createUser(UserRegisterRequestDto dto) {
        User user = new User();
        user.name = dto.getName();
        user.email = dto.getEmail();
        user.password = dto.getPassword();
        user.imageUrl = "";
        user.introduce = "";
        user.level = 0;
        user.interests = new Interest(Category.OS, Category.NETWORK, Category.DATA_STRUCTURE);
        user.role = Role.MENTEE;

        return user;
    }

    @Builder
    public User(String name, String email, String imageUrl, Role role) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public User update(String name, String profile) {
        this.name = name;
        this.imageUrl = profile;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
