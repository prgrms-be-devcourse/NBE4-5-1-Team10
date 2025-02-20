package nbe341team10.coffeeproject.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    private String username;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;    // 권한

}
