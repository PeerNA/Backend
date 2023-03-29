package cos.peerna.controller.dto;

import lombok.Data;

@Data
public class UserRegisterRequestDto {
    private String name;
    private String email;
    private String password;
}



