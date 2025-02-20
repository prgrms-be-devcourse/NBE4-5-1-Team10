package nbe341team10.coffeeproject.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nbe341team10.coffeeproject.global.entity.BaseTime;

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

