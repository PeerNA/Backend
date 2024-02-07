package cos.peerna.global.security.dto;

import cos.peerna.domain.user.model.Career;
import cos.peerna.domain.user.model.Interest;
import cos.peerna.domain.user.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class SessionUser implements UserDetails {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String imageUrl;
    private String token;
    private String login;
    private Integer score;

    public SessionUser(User user, String token, String login) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.imageUrl = user.getImageUrl();
        this.score = user.getScore();
        this.token = token;
        this.login = login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
