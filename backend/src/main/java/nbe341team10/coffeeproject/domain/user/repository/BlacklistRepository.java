package nbe341team10.coffeeproject.domain.user.repository;

import nbe341team10.coffeeproject.domain.user.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    boolean existsByToken(String token);

    @Transactional
    Blacklist save(Blacklist blacklist);

    void deleteAllByExpiryDateBefore(LocalDateTime expiryDate);
}
