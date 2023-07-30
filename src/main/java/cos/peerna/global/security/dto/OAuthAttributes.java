package cos.peerna.global.security.dto;

import cos.peerna.domain.user.model.Role;
import cos.peerna.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private Long id;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String imageUrl;
    private String bio;
    private String login;
    private String token;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, Long id,
                           String nameAttributeKey, String name,
                           String email, String imageUrl, String bio,
                           String token, String login) {
        this.id = id;
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.bio = bio;
        this.token = token;
        this.login = login;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes,
                                     String userEmail, String token) {
        return ofGitHub(userNameAttributeName, attributes, userEmail, token);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .imageUrl((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofGitHub(String userNameAttributeName,
                                            Map<String, Object> attributes,
                                            String userEmail, String token) {
        Long id = Long.parseLong(String.valueOf(attributes.get("id")));
        String login = (String) attributes.get("login");
        String name = (String) attributes.get("name");
        String avatarUrl = (String) attributes.get("avatar_url");
        String bio = (String) attributes.get("bio");

        return OAuthAttributes.builder()
                .id(id)
                .name(name)
                .email(userEmail)
                .login(login)
                .token(token)
                .imageUrl(avatarUrl)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .bio(bio)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .imageUrl(imageUrl)
                .bio(bio)
                .role(Role.MENTEE)
                .build();
    }
}
