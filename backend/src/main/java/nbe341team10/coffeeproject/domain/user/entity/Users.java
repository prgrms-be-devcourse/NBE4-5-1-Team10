package nbe341team10.coffeeproject.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Users extends BaseTime {
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

