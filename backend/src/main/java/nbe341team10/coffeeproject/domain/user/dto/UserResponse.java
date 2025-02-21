package nbe341team10.coffeeproject.domain.user.dto;

import lombok.Data;
import nbe341team10.coffeeproject.domain.user.entity.Users;

@Data
public class UserResponse {
    private String username;
    private String email;
    private String address;
    private String role;
    //private String authority;   // 권한

    public UserResponse(Users user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.role = user.getRole().name();
    }
    

}
