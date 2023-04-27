package cos.peerna.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
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



