package nbe341team10.coffeeproject.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nbe341team10.coffeeproject.global.entity.BaseTime;
import org.springframework.lang.NonNull;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Users extends BaseTime {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;    // 권한
}

