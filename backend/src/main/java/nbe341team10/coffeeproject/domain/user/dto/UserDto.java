package nbe341team10.coffeeproject.domain.user.dto;

import lombok.Getter;
import nbe341team10.coffeeproject.domain.user.entity.Role;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import org.springframework.lang.NonNull;

@Getter
public class UserDto {
    @NonNull
    private long id;
    @NonNull
    private String email;
    @NonNull
    private String username;
    @NonNull
    private Role role;


    public UserDto(Users user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
