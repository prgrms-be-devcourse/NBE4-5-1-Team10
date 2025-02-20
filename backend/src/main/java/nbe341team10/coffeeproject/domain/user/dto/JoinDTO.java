package nbe341team10.coffeeproject.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinDTO {
    private String username;
    private String email;
    private String password;
    private String address;
}
