package nbe341team10.coffeeproject.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nbe341team10.coffeeproject.global.entity.BaseTime;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Blacklist extends BaseTime {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    // 만료시간
    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @PrePersist
    public void prePersist() {
        this.expiryDate = this.getCreatedAt().plusHours(1);   // 생성시간 가져와서 +1 시간 만료
    }

}
