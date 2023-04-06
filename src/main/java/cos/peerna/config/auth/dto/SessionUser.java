package cos.peerna.config.auth.dto;

import cos.peerna.domain.Career;
import cos.peerna.domain.Interest;
import cos.peerna.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Getter
public class SessionUser implements Serializable, UserDetails {
    private final String name;
    private final String email;
    private final String password;
    private final String imageUrl;
    private final Interest interest;
    private final Career career;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.imageUrl = user.getImageUrl();
        this.interest = user.getInterests();
        this.career = user.getCareer();
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
