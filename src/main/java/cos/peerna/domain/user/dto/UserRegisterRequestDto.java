package cos.peerna.domain.user.dto;

import lombok.*;

@Data
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRegisterRequestDto {
    private Long id;
    private String name;
    private String email;
    private String password;

    @Builder
    public UserRegisterRequestDto(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}



