package nbe341team10.coffeeproject.DTO.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinDTO {
    private String username;
    private String email;
    private String password;
    private String address;
}
