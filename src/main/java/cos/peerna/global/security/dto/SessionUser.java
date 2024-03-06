package cos.peerna.global.security.dto;

import cos.peerna.domain.user.model.Category;
import cos.peerna.domain.user.model.Role;
import cos.peerna.domain.user.model.User;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@ToString
public class SessionUser implements UserDetails {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String imageUrl;
    private String token;
    private String login;
    private Integer score;
    private final List<GrantedAuthority> grantedAuthorities;

    public SessionUser(User user, String token, List<GrantedAuthority> authorities, String login) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.imageUrl = user.getImageUrl();
        this.score = user.getScore();
        this.token = token;
        this.login = login;
        this.grantedAuthorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id.toString();
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