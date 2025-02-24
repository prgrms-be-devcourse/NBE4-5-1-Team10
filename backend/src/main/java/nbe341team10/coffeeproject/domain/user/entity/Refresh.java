package nbe341team10.coffeeproject.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import nbe341team10.coffeeproject.global.entity.BaseTime;

@Entity
@Getter
@Setter
public class Refresh extends BaseTime {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String refresh;

    // 만료
    private String expiration;

}
