package cos.peerna.domain.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ANONYMOUS("ANONYMOUS", "익명"),
    USER("USER", "유저"),
    ADMIN("ADMIN", "관리자");

    private final String key;
    private final String title;
}
